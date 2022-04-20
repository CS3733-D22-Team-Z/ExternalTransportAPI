package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.ExternalPatientTransportAPI;
import edu.wpi.cs3733.D22.teamZ.entity.ExternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.entity.RequestStatus;
import edu.wpi.cs3733.D22.teamZ.entity.TransportMethod;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ExternalPatientTransportationRequestController implements Initializable {
  @FXML private Label handlerIDLabel;
  @FXML private Label issuerIDLabel;
  @FXML private Label departureDateLabel;
  @FXML private Label destinationLabel;
  @FXML private Label patientIdLabel;
  @FXML private Label transportMethodLabel;
  @FXML private Label descriptionLabel;
  @FXML private Label titleLabel;
  @FXML private Button submitButton;
  @FXML private Button resetButton;
  @FXML private MFXTextField patientIDField;
  @FXML private MFXTextField destinationField;
  @FXML private MFXDatePicker departureDateField;
  @FXML private Label successfulSubmitLabel;
  @FXML private Label errorSavingLabel;
  @FXML private Rectangle warningBackground;
  @FXML private MFXTextField issuerIDField;
  @FXML private MFXTextField handlerIDField;
  @FXML private ComboBox<String> transportMethodComboBox;
  @FXML private MFXButton requestListButton;

  private final String toLandingPageURL =
      "edu/wpi/cs3733/D22/teamZ/views/ExternalPatientTransportListController.fxml";

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    // menuName = "External Patient Transportation Request";
    errorSavingLabel.setVisible(false);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
    submitButton.setDisable(true);
    transportMethodComboBox.setItems(
        FXCollections.observableArrayList("HELICOPTER", "AMBULANCE", "PATIENTCAR", "PLANE"));

    if (!ExternalPatientTransportAPI.getRunArgsStrings().isEmpty()) {
      destinationField.setText(ExternalPatientTransportAPI.getRunArgsStrings().get(1));
    }
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent event) {
    ExternalPatientTransportAPI externalPatientTransportAPI =
        ExternalPatientTransportAPI.getInstance();
    List<ExternalTransportRequest> serviceRequestList =
        externalPatientTransportAPI.getAllExternalTransportRequests();
    RequestStatus requestStatus = RequestStatus.PROCESSING;

    // Create entities for submission
    String requestID = externalPatientTransportAPI.getNewUniqueExternalTransportID();
    String issuer = issuerIDField.getText();

    // String handler = handlerIDField.getText();
    String handler;
    if (handlerIDField.getText().isEmpty()) {
      requestStatus = RequestStatus.UNASSIGNED;
      handler = "";
    } else handler = handlerIDField.getText();

    String patientID = patientIDField.getText();
    String destination = destinationField.getText();
    LocalDate departureDate = departureDateField.getValue();
    TransportMethod transportMethod =
        TransportMethod.fromString(transportMethodComboBox.getSelectionModel().getSelectedItem());
    ExternalTransportRequest temp =
        new ExternalTransportRequest(
            requestID,
            requestStatus,
            issuer,
            handler,
            patientID,
            destination,
            departureDate,
            transportMethod);
    if (externalPatientTransportAPI.addExternalTransportRequest(temp)) {
      System.out.println("successful addition of patient transport request");
      successfulSubmitLabel.setVisible(true);
    } else {
      System.out.println("failed addition of patient transport request");
      warningBackground.setVisible(true);
    }
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) {
    issuerIDField.clear();
    handlerIDField.clear();
    patientIDField.clear();
    destinationField.clear();
    transportMethodComboBox.getSelectionModel().clearSelection();
    // departureTimeField.clear();
    departureDateField.setValue(null);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
  }

  @FXML
  protected void validateButton() {
    if (!issuerIDField.getText().trim().isEmpty()
        && !patientIDField.getText().trim().isEmpty()
        && !destinationField.getText().trim().isEmpty()
        // && !departureTimeField.getText().trim().isEmpty()
        && !departureDateField.getText().trim().isEmpty()
        && transportMethodComboBox.getButtonCell().isSelected()) {
      submitButton.setDisable(false);
    }
  }

  public void onRequestListButtonClicked(ActionEvent actionEvent) throws IOException {
    Stage mainStage = (Stage) requestListButton.getScene().getWindow();
    Parent root =
        FXMLLoader.load(App.class.getResource("views/ExternalPatientTransportRequestList.fxml"));
    Scene scene = new Scene(root);

    scene.getStylesheets().add(App.getCssPath());

    mainStage.setScene(scene);
  }
}
