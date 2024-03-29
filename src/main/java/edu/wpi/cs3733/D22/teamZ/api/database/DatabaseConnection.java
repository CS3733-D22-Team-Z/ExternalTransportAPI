package edu.wpi.cs3733.D22.teamZ.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnection {

  private static Connection con = null;

  static {
    String url = "jdbc:derby:externalTransportDB;create=true";
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      con = DriverManager.getConnection(url);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  static Connection getConnection() {
    return con;
  }
}
