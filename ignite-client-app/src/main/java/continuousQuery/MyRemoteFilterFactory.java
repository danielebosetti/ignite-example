package continuousQuery;

import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEventFilter;
import org.apache.ignite.binary.BinaryObject;
import domain.Product;

/**
 * remote event filter factory
 */
public class MyRemoteFilterFactory implements Factory<CacheEntryEventFilter<Object, BinaryObject>> {
  private static final long serialVersionUID = 1L;

  @Override
  public CacheEntryEventFilter<Object, BinaryObject> create() {
    return new MyEntryEventFilter();
  }

}
