package edu.wpi.cs3733.D22.teamZ.api.database;

import edu.wpi.cs3733.D22.teamZ.api.entity.ExternalTransportRequest;
import java.io.File;
import java.util.List;
import lombok.NonNull;

public class FacadeDAO {
  private static final FacadeDAO instance = new FacadeDAO();

  private final edu.wpi.cs3733.D22.teamZ.api.database.ExternalTransportDAOImpl externalTransportDAO;

  public static FacadeDAO getInstance() {
    return instance;
  }

  private FacadeDAO() {
    externalTransportDAO = new edu.wpi.cs3733.D22.teamZ.api.database.ExternalTransportDAOImpl();
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
}
