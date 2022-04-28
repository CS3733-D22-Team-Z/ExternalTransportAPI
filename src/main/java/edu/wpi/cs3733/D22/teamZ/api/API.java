package edu.wpi.cs3733.D22.teamZ.api;

import edu.wpi.cs3733.D22.teamZ.api.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.api.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.api.entity.ExternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import lombok.NonNull;

public class API {
  private FacadeDAO facadeDAO;
  private boolean successfulInitialize;

  /** Initializes an instance of this API by recreating all necessary tables. */
  public API() {
    DBInitializer init = new DBInitializer();
    this.successfulInitialize = init.createTable();
    this.facadeDAO = FacadeDAO.getInstance();
  }

  /**
   * @param xCoord The x-coordinate for the location of the screen.
   * @param yCoord The y-coordinate for the location of the screen.
   * @param windowWidth This parameter does not affect anything in the program. The default width is
   *     450.
   * @param windowLength This parameter does not affect anything in the program. The default length
   *     is 800.
   * @param cssPath The relative path of the CSS file used to format the API window.
   * @param destLocationID The destination that will be filled in of this service request.
   * @param originLocationID This field is not used in this API.
   * @throws ServiceException If an error has occurred when creating the table on initialization.
   */
  public void run(
      int xCoord,
      int yCoord,
      int windowWidth,
      int windowLength,
      String cssPath,
      String destLocationID,
      String originLocationID)
      throws ServiceException {

    if (!successfulInitialize) {
      throw new ServiceException("The database was not able to be initialized");
    }

    ArrayList<Integer> runArgsInts = new ArrayList<>();
    runArgsInts.add(xCoord);
    runArgsInts.add(yCoord);
    runArgsInts.add(windowWidth);
    runArgsInts.add(windowLength);
    ArrayList<String> runArgsStrings = new ArrayList<>();
    runArgsStrings.add(cssPath);
    runArgsStrings.add(destLocationID);
    runArgsStrings.add(originLocationID);

    App.setRunArgsInts(runArgsInts);
    App.setRunArgsStrings(runArgsStrings);

    try {
      App.launch(App.class);
    } catch (Exception e) {
      try {
        App.staticStart(new Stage());
      } catch (IOException ex) {
        throw new ServiceException("The css path given could not be found");
      }
    }
  }

  /**
   * Gets all ExternalTransportRequest objects currently stored.
   *
   * @return A list of all ExternalTransportRequest objects stored.
   */
  public List<ExternalTransportRequest> getAllExternalTransportRequests() {
    return facadeDAO.getAllExternalTransportRequests();
  }

  /**
   * Gets the ExternalTransportRequest currently stored with the given id or null if no
   * ExternalTransportRequest with the given id exists.
   *
   * @param id The id of the request to be searched for.
   * @return The ExternalTransportRequest object with the given id or null if no object exists.
   */
  public ExternalTransportRequest getExternalTransportRequestByID(String id) {
    return facadeDAO.getExternalTransportRequestByID(id);
  }

  /**
   * Adds the given ExternalTransportRequest to the database if no request with its id currently
   * stored.
   *
   * @param request The request to be stored.
   * @return True if the request was successfully able to be stored in the database, false
   *     otherwise.
   */
  public boolean addExternalTransportRequest(@NonNull ExternalTransportRequest request) {
    return facadeDAO.addExternalTransportRequest(request);
  }

  /**
   * Updates the ExternalTransportRequest in the database with the same id as the given request.
   *
   * @param request The request to be updated.
   * @return True if the request was successfully updated in the database, false otherwise.
   */
  public boolean updateExternalTransportRequest(@NonNull ExternalTransportRequest request) {
    return facadeDAO.updateExternalTransportRequest(request);
  }

  /**
   * Deletes the ExternalTransportRequest in the database with the given id.
   *
   * @param id The id of the ExternalTransportRequest to be deleted.
   * @return True if an ExternalTransportRequest with the given id was able to be successfully
   *     deleted from the database. If this method returns false, either an error occurred or a
   *     request with that id does not exist.
   */
  public boolean deleteExternalTransportRequest(String id) {
    return facadeDAO.deleteExternalTransportRequest(id);
  }

  /**
   * Sets the database to the information contained within the .csv file residing at the given path.
   * If this method returns false, the resulting database will revert to the state that it was in
   * before the function was called.
   *
   * @param path The File object navigating to the desired .csv file.
   * @return True if the .csv was able to be successfully imported into the database. False if the
   *     .csv file could not be imported for any reason including but not limited to IOException
   *     from the file or if there was an error when editing the database.
   */
  public boolean importExternalTransportsFromCSV(@NonNull File path) {
    return facadeDAO.importExternalTransportsFromCSV(path);
  }

  /**
   * Exports the information in the database to a .csv file residing at the given path.
   *
   * @param path The File object navigating to the desired .csv file.
   * @return True if the .csv was able to be successfully imported into the database. False if the
   *     .csv file could not be imported for any reason including but not limited to IOException
   *     from the file or if there was an error when editing the database.
   */
  public boolean exportExternalTransportsToCSV(@NonNull File path) {
    return facadeDAO.exportExternalTransportsToCSV(path);
  }
}
