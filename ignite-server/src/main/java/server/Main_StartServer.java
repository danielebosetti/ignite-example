package server;

import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 * starts an instance of apache ignite (server)
 */
public class Main_StartServer {
  
  public static void main(String[] args) throws Exception {
    System.setProperty("IGNITE_HOME", System.getProperty("user.dir")+"/target/ignite-workdir");
    String igniteHome = System.getProperty("IGNITE_HOME");
    System.out.println("IGNITE_HOME="+igniteHome);
    StartServerCommon.main(args);
  }
  
  public static void startClean(String[] args) throws Exception {
    System.setProperty("IGNITE_HOME", System.getProperty("user.dir")+"/target/ignite-workdir");
    String igniteHome = System.getProperty("IGNITE_HOME");
    System.out.println("IGNITE_HOME="+igniteHome);
    System.out.println("deleting work dir contents! dir="+igniteHome);
    FileUtils.deleteDirectory(new File(igniteHome) );
    main(args);
  }
  
}

