package server;

/**
 * starts an ignite server
 * it is in a different project from common, so that we can use different classpaths
 * (to test de/serialization, adding fields to data beans  etc.)
 * 
 * working dir is ignite-work2
 */
public class StartServer2 {
  public static void main(String[] args) throws Exception {
    Main_StartServer2.main(args);
  }
}
