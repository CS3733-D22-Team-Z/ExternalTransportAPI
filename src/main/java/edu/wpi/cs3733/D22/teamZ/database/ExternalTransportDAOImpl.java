package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ExternalTransportRequest;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

class ExternalTransportDAOImpl {
  private final Connection connection = DatabaseConnection.getConnection();
  private final HashMap<String, ExternalTransportRequest> externalTransports;
  private final ControlCSV controlCSV;

  ExternalTransportDAOImpl() {
    this.externalTransports = new HashMap<>();
    this.controlCSV = new ControlCSV();
  }

  /**
   * Gets a list of all external transport requests currently stored in the database
   *
   * @return A list of all external transport requests currently stored in the database
   */
  List<ExternalTransportRequest> getAllExternalTransportRequests() {
    return new ArrayList<>(externalTransports.values());
  }

  /**
   * Returns the transport request in the database that has the given id
   *
   * @param id The id of the request to be returned
   * @return The transport request with the given id or null if there is no request with that id
   */
  ExternalTransportRequest getExternalTransportRequestByID(String id) {
    return externalTransports.get(id);
  }

  /**
   * Adds the given request to the database if one with the same id does not exist already
   *
   * @param request The request to be added
   * @return True if the request was successfully added, false otherwise
   */
  boolean addExternalTransportRequest(@NonNull ExternalTransportRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO EXTERNALTRANSPORTREQUEST (requestID,"
                  + " status, issuerID, handlerID, patientID,"
                  + "destination, departureDate, transportMethod)"
                  + " values (?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getStatus().toString());
      stmt.setString(3, request.getIssuerID());
      stmt.setString(4, request.getHandlerID());
      stmt.setString(5, request.getPatientID());
      stmt.setString(6, request.getTransportDestination());
      stmt.setDate(7, Date.valueOf(request.getDepartureDate()));
      stmt.setString(8, request.getTransportMethod().toString());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      return false; // Failed to add
    }

    externalTransports.put(request.getRequestID(), request);
    return true;
  }

  /**
   * Uses the given request to find and update the request in the database with the same id
   *
   * @param request The request whose fields contain the updated information
   * @return True if the database was able to successfully update, false otherwise
   */
  boolean updateExternalTransportRequest(@NonNull ExternalTransportRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE externalTransportRequest SET status =?,"
                  + "issuerID =?, handlerID =?, patientID =?,"
                  + "destination =?, departureDate =?,"
                  + "transportMethod =? WHERE RequestID =?");
      stmt.setString(1, request.getStatus().toString());
      stmt.setString(2, request.getIssuerID());
      stmt.setString(3, request.getHandlerID());
      stmt.setString(4, request.getPatientID());
      stmt.setString(5, request.getTransportDestination());
      stmt.setDate(6, Date.valueOf(request.getDepartureDate()));
      stmt.setString(7, request.getTransportMethod().toString());
      stmt.setString(8, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      return false; // failed to update database
    }

    externalTransports.put(request.getRequestID(), request);
    return true;
  }

  /**
   * Deletes the given id from the database along with the object associated with it
   *
   * @param id The id of the transport request to be deleted
   * @return True if the request was successfully deleted, false otherwise
   */
  boolean deleteExternalTransportRequest(String id) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM EXTERNALTRANSPORTREQUEST WHERE REQUESTID=?");
      stmt.setString(1, id);
      stmt.executeUpdate();
      stmt.close();
      connection.commit();
    } catch (SQLException e) {
      return false;
    }

    externalTransports.remove(id);
    return true;
  }

  /**
   * Imports the data from a CSV file at the given path and saves it into the database. If this
   * process fails at any point, a backup of the database is reloaded as if nothing happened.
   *
   * @param path The file path that contains the CSV to be read
   * @return True if all data was able to be successfully imported into the database, false
   *     otherwise
   */
  boolean importExternalTransportsFromCSV(@NonNull File path) {
    // Save a backup in case things fail halfway through
    List<ExternalTransportRequest> backupList = new ArrayList<>(externalTransports.values());

    // Get database and imported lists
    List<ExternalTransportRequest> databaseRequests = getAllExternalTransportRequests();
    List<ExternalTransportRequest> importedRequests;
    try {
      importedRequests = controlCSV.importCSV(path);
    } catch (IOException e) {
      return false;
    }

    // Remove all elements that don't appear in the imported list
    {
      List<ExternalTransportRequest> notInImported =
          databaseRequests.stream()
              .filter(e -> (!importedRequests.contains(e)))
              .collect(Collectors.toList());

      boolean success = true;
      for (ExternalTransportRequest request : notInImported) {
        success = deleteExternalTransportRequest(request.getRequestID()) && success;
      }

      if (!success) {
        reloadFromList(backupList);
        return false;
      }
    }

    // Add all elements that we didn't have before
    {
      List<ExternalTransportRequest> notInDatabase =
          importedRequests.stream()
              .filter(e -> (!databaseRequests.contains(e)))
              .collect(Collectors.toList());

      boolean success = true;
      for (ExternalTransportRequest request : notInDatabase) {
        success = addExternalTransportRequest(request) && success;
      }

      if (!success) {
        reloadFromList(backupList);
        return false;
      }
    }

    // Update all elements that already exist
    {
      List<ExternalTransportRequest> commonObjects =
          importedRequests.stream().filter(databaseRequests::contains).collect(Collectors.toList());

      boolean success = true;
      for (ExternalTransportRequest request : commonObjects) {
        success = updateExternalTransportRequest(request) && success;
      }

      if (!success) {
        reloadFromList(backupList);
        return false;
      }
    }

    return true; // success!
  }

  /**
   * Exports the current database to the .csv file specified by the given path
   *
   * @param path The path to the .csv file to be written to
   * @return True if the database could be successfully exported, false otherwise
   */
  boolean exportExternalTransportsToCSV(@NonNull File path) {
    try {
      controlCSV.exportCSV(path, getAllExternalTransportRequests());
    } catch (IOException e) {
      return false;
    }

    return true;
  }

  /**
   * Generates and returns a new unique id that has not yet in the database
   *
   * @return A unique id that has not yet been stored in the database
   */
  String getNewUniqueExternalTransportID() {
    SecureRandom rng = new SecureRandom();
    String generatedID;
    do {
      String idNum = Integer.toString(rng.nextInt(Integer.MAX_VALUE));
      generatedID = "REQ" + idNum;
      ExternalTransportRequest temp = getExternalTransportRequestByID(generatedID);
      System.out.println("Thing");
    } while (getExternalTransportRequestByID(generatedID) != null);

    return generatedID;
  }

  private void reloadFromList(List<ExternalTransportRequest> requestList) {
    try {
      Statement stmt = connection.createStatement();
      // Clear all data
      stmt.execute("DELETE FROM EXTERNALTRANSPORTREQUEST");
    } catch (SQLException e) {
      // This should never happen
    }

    externalTransports.clear();

    for (ExternalTransportRequest request : requestList) {
      addExternalTransportRequest(request);
    }
  }
}
