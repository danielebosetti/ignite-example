package client.timing;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import util.CacheUtil;
import util.Util;

/**
 * reads all elements from the near cache and gets timing
 * the first read loop is slow (~10sec), after warming up it becomes faster (20ms)
 */
public class NearCacheReadingClient2 {

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      IgniteCache<Object, Object> cache = CacheUtil.getProductNearCache(ig);

      Thread worker = new ReadWorker(cache);
      worker.start();
      
      Util.pressKey();
    }
  }
}
