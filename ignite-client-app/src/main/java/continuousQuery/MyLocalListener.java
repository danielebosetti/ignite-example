package continuousQuery;

import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.ContinuousQueryWithTransformer.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import domain.Product;

/**
 * (local) listener of cache entry events 
 */
public class MyLocalListener implements EventListener<BinaryObject> {
  private static final Logger log = LoggerFactory.getLogger("test.MyLocList");
  
  @Override
  public void onUpdated(Iterable<? extends BinaryObject> events) {
    for (BinaryObject item:events) {
      log.info("onUpdated: binary={}", item);
      //Product product = item.deserialize();
      //log.info("MyLocalListener.onUpdated: deserialized={}", product);
    }
  }

}
