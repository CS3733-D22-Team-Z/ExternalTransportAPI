package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;

public class Main {

  public static void main(String[] args) {
    DBInitializer init = new DBInitializer();
    if(init.createTable()) {
      System.out.println("Success!");
      // App.launch(App.class, args);
    } else {
      System.out.println("Unable to create ExternalTransportAPI table. Aborting.");
      System.exit(-1);
    }
  }
}
