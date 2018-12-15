package client;

import javax.cache.Cache.Entry;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

/**
 * simple client browse elements from the product cache (table)
 */
public class BrowseBinaryClient {
  private static final Logger log = LoggerFactory.getLogger("test.BrowseBinaryClient");

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {

      {
        IgniteCache<Object, BinaryObject> cache =
            ig.getOrCreateCache(Util.PRODUCT_CACHE).withKeepBinary();
        for (Entry<Object, BinaryObject> item : cache) {
          Object key = item.getKey();
          BinaryObject val = item.getValue();
          log.info("pmsCode={}", (String) val.field("pmsCode"));
          log.info("key={} val={}", key, val);
        }
      }

      IgniteCache<Object, BinaryObject> cache2 =
          ig.getOrCreateCache(Util.DESK_CACHE).withKeepBinary();
      for (Entry<Object, BinaryObject> item : cache2) {
        BinaryObject val = item.getValue();
        log.info("desk={}", (String) val.field("desk"));
        log.info("item={}", val);
      }

      IgniteCache<Object, BinaryObject> cache3 =
          ig.getOrCreateCache(Util.POSITION_CACHE).withKeepBinary();
      for (Entry<Object, BinaryObject> item : cache3) {
        BinaryObject val = item.getValue();
        log.info("item={}", val);
      }

      {
        IgniteCache<Object, BinaryObject> cache = ig.getOrCreateCache("TEST").withKeepBinary();
        CacheConfiguration config = cache.getConfiguration(CacheConfiguration.class);
        log.info("config={}", config);
        for (Entry<Object, BinaryObject> item : cache) {
          Object key = item.getKey();
          BinaryObject val = item.getValue();
          log.info("key={} val={}", key, val);
        }
      }

      {
        IgniteCache<Object, BinaryObject> cache = ig.getOrCreateCache("test").withKeepBinary();
        CacheConfiguration config = cache.getConfiguration(CacheConfiguration.class);
        log.info("config={}", config);
        for (Entry<Object, BinaryObject> item : cache) {
          Object key = item.getKey();
          BinaryObject val = item.getValue();
          log.info("key={} val={}", key, val);
        }
      }
    }
  }
}
