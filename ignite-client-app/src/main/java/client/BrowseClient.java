package client;

import javax.cache.Cache.Entry;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

/**
 * simple client browse elements from the product cache (table)
 */
public class BrowseClient {
  private static final Logger log = LoggerFactory.getLogger("test.BrowseClient");

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {

      IgniteCache<Object, Object> cache = ig.cache(Util.PRODUCT_CACHE);
      for (Entry<Object, Object> item : cache) {
        log.info("item={}", item);
      }

      IgniteCache<Object, Object> cache2 = ig.cache(Util.DESK_CACHE);
      for (Entry<Object, Object> item : cache2) {
        log.info("item={}", item);
      }

      IgniteCache<Object, Object> cache3 = ig.cache(Util.POSITION_CACHE);
      for (Entry<Object, Object> item : cache3) {
        log.info("item={}", item);
      }
    }
  }
}
