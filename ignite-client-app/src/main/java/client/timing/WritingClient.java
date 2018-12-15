package client.timing;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import domain.Product;
import util.Util;

/**
 * writes 10k elements, gets timing
 * 
 */
public class WritingClient {

  public static void main(String[] args) {

    int numWrites = Util.MAX_INDEX;

    Product[] v = new Product[numWrites];
    for (int i = 0; i < numWrites; i++) {
      Product p = new Product( Util.getKey(i) );
      v[i] = p;
    }

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      IgniteCache<Object, Object> cache = ig.getOrCreateCache(Util.PRODUCT_CACHE);

      Thread worker = new Thread(() -> {
        while (true) {
          long start = System.currentTimeMillis();
          int c=0;
          for (Product item : v) {
            if (++c%100==0) {System.out.print("."); }
            cache.put(item.getPmsCode(), item);
          }
          long delta = System.currentTimeMillis() - start;
          System.out.println("written num=" + numWrites + " items, elapsed(msec)=" + delta);
        }
      });
      worker.setDaemon(true);
      worker.start();
      Util.pressKey();
    }
  }
}
