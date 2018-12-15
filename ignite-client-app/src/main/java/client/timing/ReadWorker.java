package client.timing;

import org.apache.ignite.IgniteCache;
import util.Util;

/**
 * loops through a key set, gets timing
 */
public class ReadWorker extends Thread {

  private final IgniteCache<Object, Object> cache;

  public ReadWorker(IgniteCache<Object, Object> cache) {
    this.cache = cache;
    setDaemon(true);
  }
  
  @Override
  public void run() {
    while (true) {
      int numReads=0;
      long start = System.currentTimeMillis();
      for (int i=0;i<Util.MAX_INDEX;i++){
        if ((i+1)%100==0) {System.out.print("."); }
        cache.get(Util.getKey(i));
        numReads++;
      }
      long delta = System.currentTimeMillis() - start;
      System.out.println("read num=" + numReads + " items, elapsed(msec)=" + delta);
    }
  }
}
