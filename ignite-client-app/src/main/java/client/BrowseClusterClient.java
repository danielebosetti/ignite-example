package client;

import java.util.Collection;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.BaselineNode;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;

/**
 * simple client
 * browse the cluster components, prints out the list of known server plus some info
 */
public class BrowseClusterClient {

  public static void main(String[] args) {
  
    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      
      ClusterGroup servers = ig.cluster().forServers();
      Collection<ClusterNode> nodes = servers.nodes();
      System.out.println("\n\nserver nodes:");
      for (ClusterNode item:nodes) {
        System.out.println(">  server-tcp-settings="+item);
        System.out.println("   server-tcp-port="+item.attribute("org.apache.ignite.user.name"));
        System.out.println("   server-id="+item.consistentId());
        System.out.println("   server-tcp-port="+item.attribute("TcpCommunicationSpi.comm.tcp.port"));
      }
      
      Collection<BaselineNode> baselineTopo = ig.cluster().currentBaselineTopology();
      System.out.println("\nbaseline topology:");
      for (BaselineNode item:baselineTopo) {
        System.out.println("id="+item.consistentId());
      }
    }
  }
}
