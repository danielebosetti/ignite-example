package controller;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

public class ControllerFactory {

  /**
   * returns a controller with a started ignite instance
   */
  public static IgniteController newController() {
    Ignite ignite = Ignition.start("ignite-client.xml");
    IgniteController res = new IgniteController(ignite);
    return res ;
  }
}
