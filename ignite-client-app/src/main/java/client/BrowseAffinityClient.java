package client;

import javax.cache.Cache.Entry;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import util.Util;

/**
 * simple client
 * browse affinity: given a cache, find out which node contains the primary elements
 */
public class BrowseAffinityClient {

  public static void main(String[] args) {
  
    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      
      String cacheName = Util.PRODUCT_CACHE;
      Affinity<Object> affinity = ig.affinity(cacheName);
      
      IgniteCache<Object, Object> cache = ig.getOrCreateCache(cacheName).withKeepBinary();
      System.out.println("cache size="+cache.size(CachePeekMode.ALL));
      for (Entry<Object, Object> item:cache) {
        Object key = item.getKey();
        ClusterNode node = affinity.mapKeyToNode(key);
        System.out.println("key="+key+ ": primary node is " + node.attributes().get("TcpCommunicationSpi.comm.tcp.host.names")+"__"+node.consistentId());
      }
    }
  }
}
