package edu.wpi.cs3733.D22.teamZ.api.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

  private final Connection connection = DatabaseConnection.getConnection();

  public boolean createTable() {
    if (connection == null) {
      // System.out.println("API database connection is null.");
      return false;
    }

    Statement stmt;
    try {
      stmt = connection.createStatement();
    } catch (SQLException e) {
      // System.out.println("Failed to access API database.");
      return false;
    }

    try {
      stmt.execute("DROP TABLE ExternalTransportRequest");
    } catch (SQLException e) {
      // Table already does not exist
    }

    try {
      stmt.execute(
          "CREATE TABLE ExternalTransportRequest ("
              + "requestID VARCHAR(15),"
              + "status VARCHAR(20),"
              + "issuerID VARCHAR(15),"
              + "handlerID VARCHAR(15),"
              + "patientID VARCHAR(15),"
              + "destination VARCHAR(50),"
              + "departureDate DATE,"
              + "transportMethod VARCHAR(20),"
              + "constraint TRANSPORTREQUEST_PK PRIMARY KEY (requestID))");
    } catch (SQLException e) {
      // System.out.println("Failed to create ExternalTransport API table.");
      // e.printStackTrace();
      return false;
    }

    return true;
  }
}
