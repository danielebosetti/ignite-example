package cache.loader;

import java.io.Serializable;
import java.util.Map;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;

public class MyCacheLoader implements CacheLoader<Object, Object>, Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public Object load(Object key) throws CacheLoaderException {
    System.out.println("MyCacheLoader.load: called with key="+key);
    return null;
  }

  @Override
  public Map<Object, Object> loadAll(Iterable<? extends Object> keys) throws CacheLoaderException {
    System.out.print("MyCacheLoader.loadAll: called with keys= ");
    for (Object k:keys) { System.out.print(k+" ");}
    System.out.println();
    return null;
  }

}
