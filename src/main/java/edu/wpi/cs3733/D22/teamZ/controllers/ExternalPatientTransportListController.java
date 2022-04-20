package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.ExternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.entity.RequestStatus;
import edu.wpi.cs3733.D22.teamZ.entity.TransportMethod;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExternalPatientTransportListController implements Initializable {
  @FXML private Label requestListLabel;
  @FXML private TableView externalTransportRequestTable;
  @FXML private MFXButton exitButton;
  @FXML private MFXButton importCSVButton;
  @FXML private MFXButton exportToCSVButton;
  @FXML private TableColumn<ExternalTransportRequest, String> patientID;
  @FXML private TableColumn<ExternalTransportRequest, RequestStatus> status;
  @FXML private TableColumn<ExternalTransportRequest, String> requestID;
  @FXML private TableColumn<ExternalTransportRequest, String> issuer;
  @FXML private TableColumn<ExternalTransportRequest, String> handler;
  @FXML private TableColumn<ExternalTransportRequest, LocalDate> departureDate;
  @FXML private TableColumn<ExternalTransportRequest, String> destination;
  @FXML private TableColumn<ExternalTransportRequest, TransportMethod> transportType;
  @FXML private MFXButton labServiceRequestButton;

  private final String toLabServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";

  private FacadeDAO facadeDAO;

  @FXML
  private void toLabServiceRequest(ActionEvent event) throws IOException {
    Stage mainStage = (Stage) labServiceRequestButton.getScene().getWindow();
    Parent root =
        FXMLLoader.load(App.class.getResource("views/ExternalPatientTransportationRequest.fxml"));
    Scene scene = new Scene(root);

    scene.getStylesheets().add(App.getCssPath());

    mainStage.setScene(scene);
  }

  // loadDataFromDatabase when button loadData is clicked
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();

    System.out.println("loading data");
    externalTransportRequestTable.getItems().clear();

    refreshTable();
  }

  public void refreshTable() {
    ObservableList<ExternalTransportRequest> data =
        FXCollections.observableList(facadeDAO.getAllExternalTransportRequests());

    // link columnNames to data
    patientID.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, String>("patientID"));
    status.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, RequestStatus>("Status"));
    requestID.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, String>("RequestID"));
    issuer.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, String>("issuerID"));
    handler.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, String>("handlerID"));
    transportType.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, TransportMethod>("transportMethod"));
    departureDate.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, LocalDate>("departureDate"));
    destination.setCellValueFactory(
        new PropertyValueFactory<ExternalTransportRequest, String>("transportDestination"));

    // load data into tableView

    externalTransportRequestTable.setItems(data);
  }

  public void onExportToCSVButtonClicked(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultLocationCSVPath();

    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      facadeDAO.exportExternalTransportsToCSV(file);
    }
  }

  public void onImportCSVButtonClicked(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setInitialDirectory(facadeDAO.getDefaultLocationCSVPath());

    File defaultFile = facadeDAO.getDefaultLocationCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showOpenDialog(stage);

    if (facadeDAO.importExternalTransportsFromCSV(file)) {
      System.out.println("Successful Import");
      refreshTable();
    } else {
      System.out.println("Import failed");
    }
  }

  public void onExitButtonPressed(ActionEvent actionEvent) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
