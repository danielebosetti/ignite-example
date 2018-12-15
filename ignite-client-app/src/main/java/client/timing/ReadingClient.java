package client.timing;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import util.CacheUtil;
import util.Util;

/**
 * reads all elements, gets timing
 * 
 */
public class ReadingClient {

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      IgniteCache<Object, Object> cache = CacheUtil.getProductCache(ig);

      Thread worker = new IterateReadWorker(cache);
      worker.start();
      
      Util.pressKey();
    }
  }
}
