package client.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client
 * creates the product table and populates it with data
 */
public class SqlUpdateClient {

  public static void main(String[] args) throws Exception {

    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
      Connection conn = DriverManager.getConnection("jdbc:ignite:thin://localhost");

      conn.createStatement().execute(" update PRODUCT set assetType='EI' where pmsCode = 'X-1234';        ");
      conn.createStatement().execute(" update PRODUCT set assetType='Currencies' where pmsCode = 'X-1234';    ");
      conn.createStatement().execute(" update PRODUCT set somefield1='THIS IS NOT A TEST' where pmsCode = 'X-1234'; ");
    }
  }
}
