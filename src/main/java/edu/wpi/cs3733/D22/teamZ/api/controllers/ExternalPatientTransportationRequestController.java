package edu.wpi.cs3733.D22.teamZ.api.controllers;

import edu.wpi.cs3733.D22.teamZ.api.*;
import edu.wpi.cs3733.D22.teamZ.api.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.api.entity.ExternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.api.entity.RequestStatus;
import edu.wpi.cs3733.D22.teamZ.api.entity.TransportMethod;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.Setter;

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
  @FXML private MFXTextField issuerIDField;
  @FXML private MFXTextField handlerIDField;
  @FXML private ComboBox<String> transportMethodComboBox;
  @FXML private MFXButton requestListButton;

  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  private final String toLandingPageURL =
      "edu/wpi/cs3733/D22/teamZ/api/views/ExternalPatientTransportListController.fxml";

  @Setter @NonNull private static String destinationFieldString;

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    // menuName = "External Patient Transportation Request";
    errorSavingLabel.setVisible(false);
    successfulSubmitLabel.setVisible(false);
    submitButton.setDisable(true);
    transportMethodComboBox.setItems(
        FXCollections.observableArrayList("HELICOPTER", "AMBULANCE", "PATIENTCAR", "PLANE"));

    destinationField.setText(destinationFieldString);

    transportMethodComboBox.valueProperty().addListener(e -> validateButton());
    departureDateField.valueProperty().addListener(e -> validateButton());
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent event) {
    List<ExternalTransportRequest> serviceRequestList = facadeDAO.getAllExternalTransportRequests();
    RequestStatus requestStatus = RequestStatus.PROCESSING;

    // Create entities for submission
    String requestID = facadeDAO.getNewUniqueExternalTransportID();
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

    if (departureDate == null) {
      successfulSubmitLabel.setVisible(false);
      errorSavingLabel.setVisible(true);
    } else {
      assert transportMethod != null;
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
      if (facadeDAO.addExternalTransportRequest(temp)) {
        // System.out.println("successful addition of patient transport request");
        successfulSubmitLabel.setVisible(true);
        errorSavingLabel.setVisible(false);
      } else {
        // System.out.println("failed addition of patient transport request");
        successfulSubmitLabel.setVisible(false);
        errorSavingLabel.setVisible(true);
      }
    }
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) {
    issuerIDField.clear();
    handlerIDField.clear();
    patientIDField.clear();
    destinationField.clear();
    transportMethodComboBox.getSelectionModel().clearSelection();
    departureDateField.setValue(null);

    submitButton.setDisable(true);

    successfulSubmitLabel.setVisible(false);
    errorSavingLabel.setVisible(false);
  }

  @FXML
  protected void validateButton() {
    if (!issuerIDField.getText().trim().isEmpty()
        && !patientIDField.getText().trim().isEmpty()
        && !destinationField.getText().trim().isEmpty()
        && !departureDateField.getText().trim().isEmpty()
        && !(transportMethodComboBox.getSelectionModel().getSelectedItem() == null)) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
    }

    successfulSubmitLabel.setVisible(false);
    errorSavingLabel.setVisible(false);
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
