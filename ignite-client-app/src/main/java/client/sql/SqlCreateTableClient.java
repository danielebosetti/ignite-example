package client.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client
 * updates product items
 */
public class SqlCreateTableClient {

  public static void main(String[] args) throws Exception {

    try ( Ignite ig = Ignition.start("ignite-client.xml") ) {
      Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
      Connection conn = DriverManager.getConnection("jdbc:ignite:thin://localhost");

//      conn.createStatement().execute("drop table product;");
      conn.createStatement().execute(
          " create table product (baseName VARCHAR(50), PMSCODE VARCHAR(50), someField1 VARCHAR(50), assetType VARCHAR(50),"
          + " PRIMARY KEY(PMSCODE) ) "
          + " WITH \"template=replicated, CACHE_NAME=product, key_type=java.lang.String, value_type=domain.Product \" ;");

      conn.createStatement().execute(" insert into product VALUES('GERMAN30.SPOT.EUR', 'X-1233', 'test', 'EI');    ");
      conn.createStatement().execute(" insert into product VALUES('EURUSD.SPOT', 'X-1234', 'test', 'Currencies');  ");
      conn.createStatement().execute(" insert into product VALUES('USDEUR.SPOT', 'X-1235', 'test', 'Currencies');  ");
      
      Thread.sleep(2000L);
    }
  }
}
