package client.timing;

import javax.cache.Cache.Entry;
import org.apache.ignite.IgniteCache;

/**
 * loops through the cache, gets timing
 */
public class IterateReadWorker extends Thread {

  private final IgniteCache<Object, Object> cache;

  public IterateReadWorker(IgniteCache<Object, Object> cache) {
    this.cache = cache;
    setDaemon(true);
  }
  
  @SuppressWarnings("unused")
  @Override
  public void run() {
      while (true) {
        int numReads=0;
        long start = System.currentTimeMillis();
        for (Entry<Object, Object> item:cache) {
          numReads++;
        }
        long delta = System.currentTimeMillis() - start;

        System.out.println("read num=" + numReads + " items, elapsed(msec)=" + delta);
      }
  }
}
