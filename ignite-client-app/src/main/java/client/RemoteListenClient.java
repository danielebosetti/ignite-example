package client;

import java.util.UUID;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.events.Event;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgnitePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

public class RemoteListenClient {
  private static final Logger log = LoggerFactory.getLogger("test.RemoteListen");

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      log.info("remote listen example");

      // Register event listeners on all nodes to listen for task events.
      // no types: all events are processed
      ig.events().remoteListen(localCallback, remoteFilter);

      Util.pressKey();
    }
  }
  
  // remote filter
  static final IgnitePredicate<Event> remoteFilter = new IgnitePredicate<Event>() {
    private static final long serialVersionUID = 1L;
    private final Logger log_ = LoggerFactory.getLogger("test.remoteFilt1");
    @Override
    public boolean apply(Event evt) {
      int type = evt.type();
      if (type==13) return false;
      log_.info("evt type={} time={}",type, new java.util.Date(evt.timestamp()));
      log_.info("evt={}",evt);
      return true;
    }
  };
  
  // local callback
  static final IgniteBiPredicate<UUID, Event> localCallback = new IgniteBiPredicate<UUID, Event>() {
    private static final long serialVersionUID = 1L;
    private final Logger log_ = LoggerFactory.getLogger("test.localCall");
    @Override
    public boolean apply(UUID nodeId, Event evt) {
      int type = evt.type();
      if (type==13) return false;
      log_.info("Received type={} ev={}",evt.type(), evt  );
      return true;
    }
  };
}
