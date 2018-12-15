package continuousQuery;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryListenerException;
import org.apache.ignite.binary.BinaryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import domain.Product;


/**
 * (remote) entry filter accepts all entries, and prints out
 */
public class MyEntryEventFilter implements CacheEntryEventFilter<Object, BinaryObject> {
  private static final Logger log = LoggerFactory.getLogger("test.MyEntryEvList");
      
  @Override
  public boolean evaluate(CacheEntryEvent<? extends Object, ? extends BinaryObject> event)
      throws CacheEntryListenerException {
    log.info("evaluate: event={}", event);
    log.info("also has domain class: {}", new Product("asd"));
    return true;
  }
}
