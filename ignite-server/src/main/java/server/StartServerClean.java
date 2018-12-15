package server;


/**
 * starts an ignite server
 * it is in a different project from common, so that we can use different classpaths
 * (to test de/serialization, adding fields to data beans  etc.)
 * 
 */
public class StartServerClean {
  public static void main(String[] args) throws Exception {
    Main_StartServer.startClean(args);
  }
}
