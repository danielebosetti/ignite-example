package client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import util.Util;

/**
 * simple client
 * browse elements from the product cache (table)
 */
public class ClearCacheClient {

  public static void main(String[] args) {
  
    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      
      String cacheName = Util.PRODUCT_CACHE;
      @SuppressWarnings("rawtypes")
      IgniteCache cache = ig.cache(cacheName);
      System.out.println("clearing cache "+cacheName);
      cache.clear();
    }
  }
}
