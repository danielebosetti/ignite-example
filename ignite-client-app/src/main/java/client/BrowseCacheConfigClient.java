package client;

import java.util.Collection;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * simple client
 * browses the caches list from he cluster, dumps settings
 */
public class BrowseCacheConfigClient {
  private static final Logger log = LoggerFactory.getLogger("test.BrowseCache");

  public static void main(String[] args) {
  
    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      
      Collection<String> cacheNames = ig.cacheNames();
      log.info("caches={}",cacheNames);
      for (String name:cacheNames) {
        IgniteCache<Object, Object> cache = ig.cache(name);
        @SuppressWarnings({"unchecked", "rawtypes"})
        CacheConfiguration config = cache.getConfiguration(CacheConfiguration.class);
        log.info("\n > cache name={}",config.getName());
        @SuppressWarnings("unchecked")
        Collection<QueryEntity> qryEntities = config.getQueryEntities();
        for (QueryEntity qe:qryEntities) {
          log.info("   eq={}", qe );
        }
      }
    }
  }
}
