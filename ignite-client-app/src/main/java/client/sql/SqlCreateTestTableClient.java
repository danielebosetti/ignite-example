package client.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client
 * updates product items
 */
public class SqlCreateTestTableClient {

  public static void main(String[] args) throws Exception {

    Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
    try ( 
        Ignite ig = Ignition.start("ignite-client.xml");
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://localhost"); 
        ) {

      conn.createStatement().execute(
          " create table test (id INT PRIMARY KEY, name varchar(50) ) " +
          " WITH \"template=replicated, CACHE_NAME=test\" ;");
      
    }
  }
}
