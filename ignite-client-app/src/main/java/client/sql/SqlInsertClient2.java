package client.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client
 * inserts product items
 */
public class SqlInsertClient2 {

  public static void main(String[] args) throws Exception {
    
    Class.forName("org.apache.ignite.IgniteJdbcDriver");
    try ( Ignite ig = Ignition.start("ignite-client.xml");
          Connection conn = DriverManager.getConnection("jdbc:ignite:cfg://cache=product@file:///C:/Users/dbosetti/Desktop/Ignite/ignite-common/src/main/resources/ignite-client.xml");
          PreparedStatement ps =conn.prepareStatement( 
              "insert into product(pmscode,assettype) VALUES(?,?);"); ) {
            
      String pmsCode = "pms-code"+System.currentTimeMillis()%100_000L;
      ps.setString(1, pmsCode);
      ps.setString(2, "FX");
      
      System.out.println("inserting pmsCode="+pmsCode);
      ps.execute();
    }
  }
}
