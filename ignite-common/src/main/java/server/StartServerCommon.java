package server;

import java.util.Collection;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import util.Util;

public class StartServerCommon {

  public static void main(String[] args) {
    // TODO edit so that it doesn't print to sys.err
    //String path = System.class.getResource("/java.util.logging.properties").getFile();
    //    System.setProperty("java.util.logging.config.file", path);
    //    System.out.println("java.util.logging.config.file="+System.getProperty("java.util.logging.config.file"));

    try ( Ignite ig = Ignition.start("ignite-server.xml") ) {
      // adds itself to the baseline topology
      // @see https://apacheignite.readme.io/docs/baseline-topology#section-setting-the-topology-from-code
      ig.cluster().active(true);
      Collection<ClusterNode> nodes = ig.cluster().forServers().nodes();
      System.out.println("updating baseline topology: setting topology="+nodes);
      ig.cluster().setBaselineTopology(nodes);

      Object consistentId = ig.cluster().localNode().consistentId();
      System.out.println("\n***************");
      System.out.println("started server id="+consistentId+"\n");
      Util.pressKey();
    }
  }
}
