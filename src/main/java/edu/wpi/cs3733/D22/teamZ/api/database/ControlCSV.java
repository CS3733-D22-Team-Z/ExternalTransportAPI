package edu.wpi.cs3733.D22.teamZ.api.database;

import edu.wpi.cs3733.D22.teamZ.api.entity.ExternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.api.entity.RequestStatus;
import edu.wpi.cs3733.D22.teamZ.api.entity.TransportMethod;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ControlCSV {
  private final String[] headers = {
    "requestID",
    "status",
    "issuerID",
    "handlerID",
    "patientID",
    "transportDestination",
    "departureDate",
    "transportMethod"
  };

  /**
   * Attempts to read in a CSV file from the given File path and returns a list of
   * ExternalTransportRequest objects containing its data
   *
   * @param path The path of the CSV file to be read
   * @return A list containing the information contained within the CSV file as
   *     ExternalTransportRequest objects
   * @throws IOException If the file does not exist, is a directory rather than a regular file, for
   *     some other reason cannot be opened for reading, or is not formatted in the expected way.
   *     Additionally, if any other I/O error occurs.
   */
  List<ExternalTransportRequest> importCSV(File path) throws IOException {
    FileReader file = new FileReader(path);
    BufferedReader input = new BufferedReader(file);

    String headerLine = input.readLine();
    String[] readHeaders = headerLine.split(",");
    if (!Arrays.equals(this.headers, readHeaders)) {
      throw new IOException("The read file was not formatted as expected");
    }

    List<ExternalTransportRequest> requestList = new ArrayList<>();

    String line;
    while ((line = input.readLine()) != null) {
      String[] fields = line.split(",");
      if (fields.length != headers.length) {
        throw new IOException("The read file was not formatted as expected");
      }

      // Parse the fields
      String requestID = fields[0];
      RequestStatus status = RequestStatus.fromString(fields[1]);
      String issuerID = fields[2];
      String handlerID = fields[3];
      String patientID = fields[4];
      String destination = fields[5];
      LocalDate departureDate = LocalDate.parse(fields[6]);
      TransportMethod transportMethod = TransportMethod.fromString(fields[7]);

      try {
        requestList.add(
            new ExternalTransportRequest(
                requestID,
                status,
                issuerID,
                handlerID,
                patientID,
                destination,
                departureDate,
                transportMethod));
      } catch (NullPointerException e) {
        throw new IOException("The read file was not formatted as expected");
      }
    }

    return requestList;
  }

  /**
   * Writes the given data to the given CSV file path
   *
   * @param path The path of the CSV to be written to
   * @param data The data that will be written to the CSV
   * @throws IOException If the file exists but is a directory rather than a regular file, does not
   *     exist but cannot be created, or cannot be opened for any other reason
   */
  void exportCSV(File path, List<ExternalTransportRequest> data) throws IOException {
    String fLine = String.join(",", headers);
    FileWriter file = new FileWriter(path);
    file.write(fLine + "\n");

    for (ExternalTransportRequest request : data) {
      String requestID = request.getRequestID();
      String status = request.getStatus().toString();
      String issuerID = request.getIssuerID();
      String handlerID = request.getHandlerID();
      String patientID = request.getPatientID();
      String destination = request.getTransportDestination();
      String departureDate = request.getDepartureDate().toString();
      String transportMethod = request.getTransportMethod().toString();

      String[] fields = {
        requestID,
        status,
        issuerID,
        handlerID,
        patientID,
        destination,
        departureDate,
        transportMethod
      };

      String line = String.join(",", fields);
      line += "\n";

      file.write(line);
    }

    file.close();
  }
}
