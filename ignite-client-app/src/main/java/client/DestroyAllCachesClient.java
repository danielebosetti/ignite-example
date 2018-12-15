package client;

import java.util.Collection;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/**
 * simple client
 * destroys all caches (beware)
 */
public class DestroyAllCachesClient {

  public static void main(String[] args) {
  
    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      destroyAllCaches(ig);
    }
  }

  public static void destroyAllCaches(Ignite ig) {
    Collection<String> cacheNames = ig.cacheNames();
    System.out.println("cachenames="+cacheNames);
 
    for (String name:cacheNames) {
      System.out.println("destroying cache name="+name);
      ig.destroyCache(name);
    }
  }
}
