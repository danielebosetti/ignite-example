package util;

import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.integration.CacheLoader;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.NearCacheConfiguration;
import cache.loader.MyCacheLoader;
import cache.loader.MyProductStore;

public class CacheUtil {

  /**
   * returns a near cache for the product cache
   */
  public static IgniteCache<Object, Object> getProductNearCache(Ignite ig) {
    String cacheName = Util.PRODUCT_CACHE;
    CacheConfiguration<Object, Object> config = new CacheConfiguration<>(cacheName);
    NearCacheConfiguration<Object, Object> nearConf = new NearCacheConfiguration<>();
    nearConf.setNearEvictionPolicyFactory(()->new LruEvictionPolicy<>(100_000));
    IgniteCache<Object, Object> cache = ig.getOrCreateCache(config, nearConf);
    return cache;
  }

  /**
   * returns a cache for the product cache
   */
  public static IgniteCache<Object, Object> getProductCache(Ignite ig) {
    String cacheName = Util.PRODUCT_CACHE;
    IgniteCache<Object, Object> cache = ig.getOrCreateCache(cacheName);
    return cache;
  }
  
  /**
   * returns a cache for the product cache, with cache loader
   */
  public static IgniteCache<Object, Object> getProductCacheWithLoader(Ignite ig) {
    String cacheName = Util.PRODUCT_CACHE + ".v1";
    
    CacheConfiguration cacheConf = new CacheConfiguration<>(cacheName);
    cacheConf.setCacheMode(CacheMode.REPLICATED);
    CacheLoader cacheLoader = new MyProductStore();
    cacheConf.setCacheStoreFactory(new FactoryBuilder.SingletonFactory(cacheLoader) );
    cacheConf.setReadThrough(true);
    
    IgniteCache<Object, Object> cache = ig.getOrCreateCache(cacheConf);
    return cache;
  }
}
