package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.ExternalPatientTransportAPI;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    ArrayList<Integer> runInts = ExternalPatientTransportAPI.getRunArgsInts();

    ArrayList<String> runStrings = ExternalPatientTransportAPI.getRunArgsStrings();

    Text text = new Text(1.0, 2.0, "Custom Font");
    Font font = Font.loadFont("resources/fonts/Montserrat-Bold.ttf", 45);
    text.setFont(font);
    text.setFill(Color.BROWN);
    text.setStroke(Color.BLUEVIOLET);
    text.setStrokeWidth(0.5);
    Parent root =
        FXMLLoader.load(App.class.getResource("views/ExternalPatientTransportRequestList.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setTitle("Team Z - External Patient Transportation API");

    primaryStage.setX(runInts.get(0));
    primaryStage.setY(runInts.get(1));
    primaryStage.setHeight(runInts.get(2));
    primaryStage.setWidth(runInts.get(3));

    root.setStyle(runStrings.get(0));

    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
