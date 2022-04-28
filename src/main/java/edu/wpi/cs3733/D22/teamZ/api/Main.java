package edu.wpi.cs3733.D22.teamZ.api;

import edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException;

class Main {

  public static void main(String[] args) {
    API api = new API();
    try {
      api.run(
          0,
          0,
          450,
          800,
          "edu/wpi/cs3733/D22/teamZ/api/styles/ServiceRequestDefault.css",
          "NYU Langone Hospital",
          "");
    } catch (ServiceException e) {
      System.out.println("Unable to create ExternalTransportAPI table. Aborting.");
      System.exit(-1);
    }
  }
}
