package client.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client
 * creates the product table
 */
public class SqlCreateTableClient2 {

  public static void main(String[] args) throws Exception {

    Class.forName("org.apache.ignite.IgniteJdbcDriver");
    try ( Ignite ig = Ignition.start("ignite-client.xml");
          Connection conn = DriverManager.getConnection("jdbc:ignite://localhost/test"); ) {

      conn.createStatement().execute("drop table if exists product;");
      conn.createStatement().execute(
          " create table product (baseName VARCHAR(50), PMSCODE VARCHAR(50) PRIMARY KEY, "
          + "someField1 VARCHAR(50), assetType VARCHAR(50) )"
          + " WITH \"template=replicated, CACHE_NAME=product, value_type=domain.Product \" ; " ) ;
      
      conn.close();
    }
  }
}
