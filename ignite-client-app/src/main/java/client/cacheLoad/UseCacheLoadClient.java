package client.cacheLoad;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import client.DestroyAllCachesClient;
import util.CacheUtil;

/**
 * 
 * cache-load is to be executed on cache miss, to load entries from the backing data source
 */
public class UseCacheLoadClient {
  public static void main(String[] args) {
    

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      
      DestroyAllCachesClient.destroyAllCaches(ig);
      
      IgniteCache<Object, Object> cache = CacheUtil.getProductCacheWithLoader(ig);
      @SuppressWarnings({"rawtypes", "unchecked"})
      CacheConfiguration conf = cache.getConfiguration(CacheConfiguration.class);
      System.out.println("conf="+conf);

      cache.loadCache( null );
      Object object = cache.get("my-key-AA__" + System.currentTimeMillis());
      System.out.println(object);
    }
  }
}
