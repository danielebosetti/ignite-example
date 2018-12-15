package client.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client
 * inserts product items
 */
public class SqlAlterTableClient {

  public static void main(String[] args) throws Exception {
    
    Class.forName("org.apache.ignite.IgniteJdbcDriver");
    try ( Ignite ig = Ignition.start("ignite-client.xml");
          Connection conn = DriverManager.getConnection("jdbc:ignite:cfg://cache=product@file:///C:/Users/dbosetti/Desktop/Ignite/ignite-common/src/main/resources/ignite-client.xml");
          Statement ps =conn.createStatement(); ) {
            
      String sql = "alter table product add column desc varchar(50);";
      System.out.println("executing: ["+sql+"]");
      ps.execute(sql);
    }
  }
}
