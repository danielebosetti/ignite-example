package continuousQuery;

import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.lang.IgniteClosure;

/**
 * remote transformer factory
 * it gives closures (java.function) to map cache entry events to cache items
 * (to be returned by the continuous query)
 */
public class MyRemoteTransformerFactory implements
    Factory<IgniteClosure<CacheEntryEvent<? extends Object, ? extends BinaryObject>, BinaryObject>> {
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  @Override
  public IgniteClosure<CacheEntryEvent<? extends Object, ? extends BinaryObject>, BinaryObject> create() {
    return new ProductSlice();
  }

}
