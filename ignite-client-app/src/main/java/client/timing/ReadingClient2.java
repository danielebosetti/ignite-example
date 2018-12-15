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
public class ReadingClient2 {

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      IgniteCache<Object, Object> cache = CacheUtil.getProductCache(ig);

      Thread worker = new ReadWorker(cache);
      worker.start();
      
      Util.pressKey();

    }
  }
}
