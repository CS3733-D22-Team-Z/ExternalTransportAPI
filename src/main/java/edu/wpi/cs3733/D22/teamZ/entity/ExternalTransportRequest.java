package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class ExternalTransportRequest {
  @NonNull private final String requestID;
  @NonNull @Setter private RequestStatus status;
  @NonNull private final String issuerID;
  @NonNull @Setter private String handlerID;
  @NonNull private final String patientID;
  @NonNull @Setter private String transportDestination;
  @NonNull @Setter private LocalDate departureDate;
  @NonNull @Setter private TransportMethod transportMethod;

  public ExternalTransportRequest(
      @NonNull String requestID,
      @NonNull RequestStatus status,
      @NonNull String issuerID,
      @NonNull String handlerID,
      @NonNull String patientID,
      @NonNull String transportDestination,
      @NonNull LocalDate departureDate,
      @NonNull TransportMethod transportMethod) {
    this.requestID = requestID;
    this.status = status;
    this.issuerID = issuerID;
    this.handlerID = handlerID;
    this.patientID = patientID;
    this.transportDestination = transportDestination;
    this.departureDate = departureDate;
    this.transportMethod = transportMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ExternalTransportRequest) {
      ExternalTransportRequest objectCast = (ExternalTransportRequest) o;
      return requestID.equals(objectCast.requestID);
    } else {
      return false;
    }
  }
}
