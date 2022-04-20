package edu.wpi.cs3733.D22.teamZ.api;

import edu.wpi.cs3733.D22.teamZ.api.controllers.ExternalPatientTransportationRequestController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Getter private static String cssPath;
  @Setter private static List<Integer> runArgsInts;
  @Setter private static List<String> runArgsStrings;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    staticStart(primaryStage);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }

  public static void staticStart(Stage primaryStage) throws IOException {
    FXMLLoader loader =
        new FXMLLoader(Main.class.getResource("views/ExternalPatientTransportRequestList.fxml"));
    AnchorPane root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setTitle("Team Z - External Patient Transportation API");

    primaryStage.setX(runArgsInts.get(0));
    primaryStage.setY(runArgsInts.get(1));
    // primaryStage.setHeight(runArgsInts.get(2));
    // primaryStage.setWidth(runArgsInts.get(3));
    primaryStage.setHeight(450);
    primaryStage.setWidth(800);

    primaryStage.setResizable(false);

    // scene.getStylesheets().clear();
    URL cssURL = App.class.getResource(runArgsStrings.get(0));
    if(cssURL != null) {
      cssPath = App.class.getResource(runArgsStrings.get(0)).toExternalForm();
    } else {
      throw new IOException();
    }
    scene.getStylesheets().add(cssPath);

    String destinationFieldString = (runArgsStrings.get(1) == null) ? "" : runArgsStrings.get(1);
    ExternalPatientTransportationRequestController.setDestinationFieldString(
        destinationFieldString);

    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/api/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
