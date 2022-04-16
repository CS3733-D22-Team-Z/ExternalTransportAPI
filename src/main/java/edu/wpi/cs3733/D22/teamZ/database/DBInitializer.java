package edu.wpi.cs3733.D22.teamZ.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

  private final Connection connection = DatabaseConnection.getConnection();

  private final ControlCSV transportControlCSV;

  public DBInitializer() {
    transportControlCSV = new ControlCSV();
  }

  public boolean createTable() {
    if (connection == null) {
      System.out.println("API database connection is null.");
      return false;
    }

    Statement stmt;
    try {
      stmt = connection.createStatement();
    } catch (SQLException e) {
      System.out.println("Failed to access API database.");
      return false;
    }

    dropExistingTable("ExternalTransportRequest");

    try {
      stmt.execute(
          "CREATE TABLE ExternalTransportRequest ("
              + "requestID VARCHAR(15),"
              + "status VARCHAR(20),"
              + "issuerID VARCHAR(15),"
              + "handlerID VARCHAR(15),"
              + "targetLocationID Varchar(15)," // TODO remove targetLocationID?
              + "patientID VARCHAR(15),"
              + "destination VARCHAR(50),"
              + "departureDate DATE,"
              + "constraint TRANSPORTREQUEST_PK PRIMARY KEY (requestID))");
    } catch (SQLException e) {
      System.out.println("Failed to create ExternalTransport API table.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private void dropExistingTable(String tableName) {
    try {
      Statement stmt = connection.createStatement();
      stmt.execute("DROP TABLE " + tableName);
    } catch (SQLException e) {
      System.out.println("Failed to drop " + tableName + " as it does not exist.");
    }
  }
}
