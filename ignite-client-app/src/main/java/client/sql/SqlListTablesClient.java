package client.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/*
 * simple client inserts product items
 */
public class SqlListTablesClient {

  public static void main(String[] args) throws Exception {

    Class.forName("org.apache.ignite.IgniteJdbcDriver");
    try (Ignite ig = Ignition.start("ignite-client.xml");
        Connection conn = DriverManager.getConnection(
            //"jdbc:ignite:cfg://cache=product@file:///C:/Users/dbosetti/Desktop/Ignite/ignite-common/src/main/resources/ignite-client.xml"
            "jdbc:ignite:thin://localhost"
            );
        Statement ps = conn.createStatement();) {

      DatabaseMetaData metadata = conn.getMetaData();
      ResultSet rs = metadata.getTables(null, null, "%", null);
      System.out.println("\n\n*****************\nTABLES:\n");
      while (rs.next()) {
        String tableName = rs.getString(3);
        System.out.println(tableName);
        ResultSet columns = metadata.getColumns(null, "PUBLIC", tableName, null);
        while(columns.next()) {
          String cols = String.format("%s  %s  %s  %s",
              columns.getString("TABLE_SCHEM"),
              columns.getString("TABLE_NAME"),
              columns.getString("COLUMN_NAME"),
              columns.getString("TYPE_NAME")
              );
          System.out.println(cols);
        }
      }
      System.out.println("\n\n*****************\n\n");
    }
  }
}
