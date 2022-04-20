package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.ExternalPatientTransportAPI;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Getter private static String cssPath;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    ArrayList<Integer> runInts = ExternalPatientTransportAPI.getRunArgsInts();
    ArrayList<String> runStrings = ExternalPatientTransportAPI.getRunArgsStrings();

    FXMLLoader loader =
        new FXMLLoader(Main.class.getResource("views/ExternalPatientTransportRequestList.fxml"));
    AnchorPane root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setTitle("Team Z - External Patient Transportation API");

    primaryStage.setX(runInts.get(0));
    primaryStage.setY(runInts.get(1));
    primaryStage.setHeight(runInts.get(2));
    primaryStage.setWidth(runInts.get(3));

    // scene.getStylesheets().clear();
    cssPath = App.class.getResource(runStrings.get(0)).toExternalForm();
    scene.getStylesheets().add(cssPath);

    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
