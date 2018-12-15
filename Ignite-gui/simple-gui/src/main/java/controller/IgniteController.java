package controller;

import org.apache.ignite.Ignite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.BeanWritingClient;

/**
 * wraps an ignite instance
 */
public class IgniteController {
  private static final Logger log = LoggerFactory.getLogger("test.IgContr");
  
  private Ignite ignite;

  public IgniteController(Ignite ignite) {
    this.ignite = ignite;
  }
  
  public void writeData(String pmsCode, String desc) {
    log.info("writedata:{} {}", pmsCode, desc);
    BeanWritingClient.writeItems(ignite, pmsCode, desc, "FX");
  }
  
  
  public void close() {
    ignite.close();
  }
  
}
