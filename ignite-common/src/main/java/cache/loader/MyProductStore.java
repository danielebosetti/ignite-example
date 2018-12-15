package cache.loader;

import java.io.Serializable;
import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;

public class MyProductStore extends CacheStoreAdapter<Object, Object> implements Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public void loadCache(IgniteBiInClosure<Object, Object> clo, Object... args) {
    //Product prod = new Product("pmsCode-" + System.currentTimeMillis());
    System.out.println("MyProductStore.loadCache: called");
    clo.apply("code-1", "will throw");
  }

  @Override
  public Object load(Object key) throws CacheLoaderException {
    System.out.println("MyProductStore.load: called");
    return null;
  }

  @Override
  public void write(Entry<? extends Object, ? extends Object> entry) throws CacheWriterException {
    System.out.println("MyProductStore.write: called");
  }

  @Override
  public void delete(Object key) throws CacheWriterException {
    System.out.println("MyProductStore.delete: called");
  }
}
