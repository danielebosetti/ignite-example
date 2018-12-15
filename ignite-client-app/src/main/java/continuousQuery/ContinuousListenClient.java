package continuousQuery;

import javax.cache.Cache.Entry;
import javax.cache.configuration.FactoryBuilder;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.ContinuousQueryWithTransformer;
import org.apache.ignite.cache.query.QueryCursor;
import util.Util;


/**
 * use continuous query
 * listens for changes in the products table; on a subset of rows and a subset of columns
 */
public class ContinuousListenClient {
  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      
      String cacheName = Util.PRODUCT_CACHE;
      IgniteCache<Object, Object> cache = ig.getOrCreateCache( cacheName ).withKeepBinary();
      
      ContinuousQueryWithTransformer<Object,BinaryObject,BinaryObject> qry = new ContinuousQueryWithTransformer<>();
       qry.setRemoteFilterFactory( new MyRemoteFilterFactory() );
//      qry.setRemoteFilterFactory(FactoryBuilder.factoryOf( MyEntryEventFilter.class ));
      qry.setLocalListener( new MyLocalListener() );

      qry.setRemoteTransformerFactory(new MyRemoteTransformerFactory());
      // skipped for now
      // qry.setInitialQuery ..
      
      QueryCursor<Entry<Object, BinaryObject>> cursor = cache.query(qry);
      for(Entry<Object, BinaryObject> item:cursor) {
        System.out.println("item="+item);
      }
      
      Util.pressKey();
    }
  }
}
