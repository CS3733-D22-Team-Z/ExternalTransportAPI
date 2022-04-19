package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.entity.ExternalTransportRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

public class ExternalPatientTransportAPI {
  private static final ExternalPatientTransportAPI instance = new ExternalPatientTransportAPI();

  private static ArrayList<Integer> runArgsInts;
  private static ArrayList<String> runArgsStrings;

  private final ExternalTransportDAOImpl externalTransportDAO;

  public static ExternalPatientTransportAPI getInstance() {
    return instance;
  }

  private ExternalPatientTransportAPI() {
    externalTransportDAO = new ExternalTransportDAOImpl();
  }

  public List<ExternalTransportRequest> getAllExternalTransportRequests() {
    return externalTransportDAO.getAllExternalTransportRequests();
  }

  public ExternalTransportRequest getExternalTransportRequestByID(String id) {
    return externalTransportDAO.getExternalTransportRequestByID(id);
  }

  public boolean addExternalTransportRequest(@NonNull ExternalTransportRequest request) {
    return externalTransportDAO.addExternalTransportRequest(request);
  }

  public boolean updateExternalTransportRequest(@NonNull ExternalTransportRequest request) {
    return externalTransportDAO.updateExternalTransportRequest(request);
  }

  public boolean deleteExternalTransportRequest(String id) {
    return externalTransportDAO.deleteExternalTransportRequest(id);
  }

  public boolean importExternalTransportsFromCSV(@NonNull File path) {
    return externalTransportDAO.importExternalTransportsFromCSV(path);
  }

  public boolean exportExternalTransportsToCSV(@NonNull File path) {
    return externalTransportDAO.exportExternalTransportsToCSV(path);
  }

  public String getNewUniqueExternalTransportID() {
    return externalTransportDAO.getNewUniqueExternalTransportID();
  }

  public File getDefaultLocationCSVPath() {
    return new File(
        System.getProperty("user.dir")
            + System.getProperty("file.separator")
            + "ExternalPatientTransportation.csv");
  }

  public void run(
      int xCoord,
      int yCoord,
      int windowWidth,
      int windowLength,
      String cssPath,
      String destLocationID,
      String originLocationID) {
    runArgsInts = new ArrayList<Integer>();
    runArgsInts.add(xCoord);
    runArgsInts.add(yCoord);
    runArgsInts.add(windowWidth);
    runArgsInts.add(windowLength);
    runArgsStrings = new ArrayList<String>();
    runArgsStrings.add(cssPath);
    runArgsStrings.add(destLocationID);
    runArgsStrings.add(originLocationID);

    App.launch(App.class, "");
  }

  public static ArrayList<Integer> getRunArgsInts() {
    return runArgsInts;
  }

  public static ArrayList<String> getRunArgsStrings() {
    return runArgsStrings;
  }
}
