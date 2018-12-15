package client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.query.ContinuousQueryWithTransformer;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteClosure;
import util.Util;

/**
 * setup:
 * C:\apps\apache-ignite-fabric-2.6.0-bin\bin> .\sqlline.bat -u jdbc:ignite:thin://127.0.0.1/
 * 
    create table product (baseName VARCHAR(50), pmsCode VARCHAR(50) PRIMARY KEY, someField1 VARCHAR(50), assetType VARCHAR(50));
   
    insert into PRODUCT VALUES('GERMAN30.SPOT.EUR', 'X-1233', 'test', 'EI');
    insert into PRODUCT VALUES('EURUSD.SPOT', 'X-1234', 'test', 'Currencies');
    insert into PRODUCT VALUES('USDEUR.SPOT', 'X-1235', 'test', 'Currencies');
    
    update PRODUCT set assetType='EI' where pmsCode = 'X-1234';
    update PRODUCT set assetType='Currencies' where pmsCode = 'X-1234';
    update PRODUCT set somefield1='THIS IS NOT A TEST' where pmsCode = 'X-1234';
    etc.
 * 
 * TODO: 
[12:43:00] Client node was reconnected after it was already considered failed by the server topology
(this could happen after all servers restarted or due to a long network outage between the client and servers). 
All continuous queries and remote event listeners created by this client will be unsubscribed, consider listening to EVT_CLIENT_NODE_RECONNECTED event to restore them.
 * 
 * @see also SqlCreateTableClient to populate the product table
 * 
 * @author twilliamson
 *
 */
public class SampleCurrenciesClient {
  
  public static void main(String[] args) {
    SampleCurrenciesClient igniteClientSample = new SampleCurrenciesClient("Currencies", Arrays.asList(new String[] {"PMSCODE","SOMEFIELD1","ASSETTYPE"}));
    igniteClientSample.init();
    igniteClientSample.start();
  }

  private Map<String, BinaryObject> currentState = new ConcurrentHashMap<>(); //TODO Can we use a nearcache for this?
  private Ignite ignite;
  private IgniteCache<Integer, BinaryObject> cache;
  private QueryCursor<Cache.Entry<String, BinaryObject>> cursor;
  private CountDownLatch initialResults;
  
  protected volatile boolean shouldRun = true;
  private final ConcreteTransformerFilter<String> transformerFilter;// = new AssetTypeTransformerFilter(); //**NOTE** cannot use lambdas in remote queries as they get cached and silently fail
  
  public SampleCurrenciesClient(String string, List<String> asList) {
    this.transformerFilter = new AssetTypeTransformerFilter(string, asList);
  }

  ///////////////////////////////////////////// start/stop/init/BBQ ////////////////////////////////////////////
  
  /**
   * Simulates a stream of ticks arriving (via tibrv say) and looks up static data for each
   */
  public void start() {
    long tickId = 0l;
    String pmsCode = null;
    while (shouldRun) {
      tickId++;
      if(tickId % 2 == 0)
        pmsCode = "X-1234";
      else
        pmsCode = "X-1235";
      
      System.out.println(new Date() + ":\t" + tickId + "\t" + pmsCode + "\t" + getFieldForPMSCode(pmsCode, "pmsCode"));
      
      try {Thread.sleep(1000l);} catch(InterruptedException e) {}
    }
  }

  protected String getFieldForPMSCode(String pmsCode, String field) {
    BinaryObject cutdownProduct = currentState.get(pmsCode);
    return cutdownProduct == null ? null : cutdownProduct.field(field);
  }
  
  public void stop() {
    shouldRun = false;
    try {ignite.close();} catch(Exception e) {}
    try {cache.close();} catch(Exception e) {}
    try {cursor.close();} catch(Exception e) {}
  }

  public void init() {
    Ignition.setClientMode(true);
    ignite = Ignition.start("ignite-client.xml");
    cache = ignite.getOrCreateCache( Util.PRODUCT_CACHE).withKeepBinary();
    initialResults = new CountDownLatch(1);// Needed as the initial runs in 'this' thread, where update/deletes turn up in an Ignite Thread
    
    ContinuousQueryWithTransformer<String, BinaryObject, Row<String>> qry = new ContinuousQueryWithTransformer<>();
    qry.setRemoteFilterFactory(() -> event -> transformerFilter.filter(event));//Filter is run before transform
    qry.setRemoteTransformerFactory(() -> event -> transformerFilter.transform(event.getKey(), event.getValue(), event.getOldValue()));
    qry.setLocalListener(updated -> {
      try {
        waitForInitial();
        
        for (Object obj : updated) {
          if(obj == null) continue;//only if the remote filter and remote transform do not match; (should not happen) this means an ignored row changed and wasn't filtered correctly
          cacheLocally(getRow(obj));
        }//TODO check that a pause at this point blocks other accesses to this block
      } catch(Exception e) {
        e.printStackTrace();
      }
    });
    
    //The 'transform' does not apply to this initial Query (instead they are manually client side transformed below). This query is filtered, as there is no point in knowing what does not match the filter on startup. 
    qry.setInitialQuery(new ScanQuery<String, BinaryObject>(transformerFilter));
    
    //start execution of the query
    cursor = cache.query(qry);
    for (Cache.Entry<String, BinaryObject> e : cursor) {// Iterate over results of 'initial' query
      //There is a bug in ContinuousQueryWithTransformer - the initial does not offer the ability to run the transformer on it, so do it client side, here. the 'row filter' is still run but each row is fully populated
      cacheLocally(transformerFilter.transform(e.getKey(), e.getValue(), null));
    }
    
    initialResults.countDown();
  }

  /*
   * Makes no special attempt at threading; a simple concurrent hashmap containing ${pmsCode}=${the rest of Product}.
   */
  private void cacheLocally(Row<String> entry) {
    System.out.println(Thread.currentThread() + " received " + entry);
    if(entry.getValue() != null) {
      currentState.put(entry.getKey(), entry.getValue());
    } else {;
      currentState.remove(entry.getKey());
    }
  }

  /*
   * This code is VERY confusing.
   * 
   * Ignite calls the localListener with an Iterable<T> (from ContinuousQueryWithTransformer<K, V, T>) - but it is not actually type T is is a BinaryObject pretending to be type 'T' when it comes off the wire. 
   * 
   * To fix this situation, check if the passed object is a BinaryObject and deserialize then cast it to the required type. I feel like this is a bug in ignite.
   * 
   */
  @SuppressWarnings("unchecked")
  private <T> T getRow(Object obj) {
    T entry = null;
    if(obj instanceof BinaryObject)
      entry = ((BinaryObject)obj).deserialize(); 
    else 
      entry = (T)obj;
    return entry;
  }

  /*
   * While the stop() method has not been called, wait for init() to complete.
   * 
   * TODO does this need an 'if(!alreadyStarted) return;' or is JIT clever enough? Lets assume it is until proven otherwise.
   */
  private void waitForInitial() {
    //wait for initial results to finish...
    while (shouldRun) { //...unless currently shutting down
      try {
        initialResults.await();
        break;
      } catch(InterruptedException e) { }//...even if interrupted
    }
  }
  
  //////////////////////////// Remotely distributed classes ///////////////////////////
  
  /**
   * TransformerFilter which filters to one asset class and a given set of fields
   * 
   * TODO this should be configurable via xpath instead of hardcoded.
   * 
   * @author twilliamson
   *
   */
  public static class AssetTypeTransformerFilter extends ConcreteTransformerFilter<String> {
    private static final long serialVersionUID = 3799450975085880901L;
    private String assetClass;

    public AssetTypeTransformerFilter(String assetClass, List<String> requiredFieldnames) {
      super(requiredFieldnames);
      this.assetClass = assetClass;
    }
    
    @Override
    public boolean apply(String k, BinaryObject v)  {
      //All filtering code must be in here; return true if this row is interesting
      return assetClass.equals(v.field("ASSETTYPE"));
    }
  }
  
  /**
   * Used to perform remote (and local) transformations and filtering for a given client's requirements.
   * 
   * MUST be concrete as lambdas are remotely cached causing unstable behaviour (disappoint).
   * 
   * @author twilliamson
   *
   * @param <T> the type of the primary key of the table being queried
   */
  public abstract static class ConcreteTransformerFilter<T> implements IgniteBiPredicate<T, BinaryObject>, Factory<IgniteClosure<CacheEntryEvent<T, BinaryObject>, Row<T>>> {
    private static final long serialVersionUID = -4017032877756059433L;
    
    private final List<String> requiredFieldNames;

    public ConcreteTransformerFilter(List<String> requiredFieldnames) {
      this.requiredFieldNames = requiredFieldnames;
    }

    //IgniteBiPredicate
    @Override
    public abstract boolean apply(T string, BinaryObject v);
    
    //Factory
    @Override
    public IgniteClosure<CacheEntryEvent<T, BinaryObject>, Row<T>> create() {
      return event -> transform(event.getKey(), event.getValue(), event.getOldValue());
    }

    /**
     * Do not return rows where oldValue exists and neither value nor oldValue match filter.
     * 
     * @param event
     * @return true if this row should be sent to the client 
     */
    public boolean filter(CacheEntryEvent<? extends T, ? extends BinaryObject> event) {
      return  event.getOldValue() == null ||
          event.getValue() == null ||
          apply(event.getKey(), event.getValue()) ||
          apply(event.getKey(), event.getOldValue());
    }
    
    /**
     * Set all fields not marked as 'interesting for this client' to null on returned Row<T>s, and return a delete marker for rows that are 'becoming unintersting'.
     * 
     * This (largely) runs on the ignite SERVER - this class is serialized and pushed from client code up to the cluster's classloader.
     *  
     * @param key the primary key of the SQL object
     * @param value the rest of the row that is changing
     * @param oldValue the rest of the row that is being 'overwritten by above value'
     * 
     * @return a Row<T> wrapper of the above key/value after transformation.
     */
    public Row<T> transform(T key, BinaryObject value, BinaryObject oldValue) {
      //run filter to determine if this transform is an 'update', 'ignore' or a 'delete'
      if(apply(key, value)) {
        //update
        System.out.println("update ["+key+"] ["+value+"] ["+oldValue+"]");
        //Make a builder of the right type (add required fields instead of cloning and removing):
        BinaryObjectBuilder builder = value.toBuilder();//Ignition.ignite().binary().builder(value.type().typeName());
        
        for (String entry : value.type().fieldNames()) {
          if(!requiredFieldNames.contains(entry))
            builder.removeField(entry);
        }//NOTE: 'key' is NOT included in 'value' as a field - so searching for 'PMSCODE' above will never happen
        
        //TODO check for 'no change' situation and return null if nothing important changed
        return new Row<>(key, builder.build());
      } else {
        //possible delete
        if(oldValue == null || !apply(key, oldValue)) {
          System.out.println("ignore ["+key+"] ["+value+"] ["+oldValue+"]");
          //ignore; there was no previous, or previous did not match either so this is not 'currently being deleted' 
          //TODO some forum comments imply that if client code mutates 'value', this will overwrite oldValue in that client's local cache, and the below will not work. Be careful and check this.
          //returning null from a transform means 'do not send a row to the client at all', but this occasionally does not work (as in a null is sometimes received by the client's localListener).
          //There is a filter above which should mean this block is never be entered
          return null; 
          
        } else {
          System.out.println("delete ["+key+"] ["+value+"] ["+oldValue+"]");
          //delete
          //TODO some options here but went with 'if value == null, this key should be deleted'
          return new Row<T>(key, null);
        }
      }
    }
  }

  /**
   * Is there one of these already?
   * 
   * Just a wrapper class to hold key/value pairs, after transform from the ignite cluster.
   * 
   * @author twilliamson
   *
   * @param <K> the type of the primary key
   */
  public static class Row<K> implements Serializable {
    private static final long serialVersionUID = 665010965239459209L;
    private K key;
    private BinaryObject value;

    public Row(K key, BinaryObject value) {
      this.key = key;
      this.value = value; 
    }
    
    public K getKey() { return key; }
    public BinaryObject getValue() { return value; }
    public String toString() { return "[key=" + key + ", value=" + value + "]"; }
  }
}