package util;

import java.util.Scanner;

public class Util {

  public static final String PRODUCT_CACHE = "product";
  public static final String DESK_CACHE = "desk";
  public static final String POSITION_CACHE = "position";
  public static final String PRODUCT_TABLE = "SQL_PUBLIC_PRODUCT";
  
  public static final int NUM_READS = 10_000;
  public static final int MAX_INDEX = 10_000;

  public static void pressKey() {
    System.out.println("press enter to exit");
    Scanner s = new Scanner(System.in);
    s.nextLine();
    s.close();
  }

  public static String getKey(int i) {
    return "key-"+(i%MAX_INDEX);
  }

  public static Object getValString(int i) {
    return "my-val__"+i+"__"+System.currentTimeMillis();
  }

}
