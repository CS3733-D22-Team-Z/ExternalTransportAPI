package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.ExternalPatientTransportAPI;

public class Main {

  public static void main(String[] args) {
    DBInitializer init = new DBInitializer();
    if (init.createTable()) {
      System.out.println("Success!");
      // App.launch(App.class, args);
      ExternalPatientTransportAPI api = ExternalPatientTransportAPI.getInstance();
      api.run(0, 0, 450, 800, "styles/ServiceRequestDefault.css", "NYU Langone Hospital", "");
    } else {
      System.out.println("Unable to create ExternalTransportAPI table. Aborting.");
      System.exit(-1);
    }
  }
}
