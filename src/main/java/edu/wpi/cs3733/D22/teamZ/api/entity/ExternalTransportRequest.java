package edu.wpi.cs3733.D22.teamZ.api.entity;

import java.time.LocalDate;
import lombok.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExternalTransportRequest {
  @EqualsAndHashCode.Include @NonNull private final String requestID;
  @NonNull @Setter private RequestStatus status;
  @NonNull private final String issuerID;
  @NonNull @Setter private String handlerID;
  @NonNull private final String patientID;
  @NonNull @Setter private String transportDestination;
  @NonNull @Setter private LocalDate departureDate;
  @NonNull @Setter private TransportMethod transportMethod;

  /**
   * Sets the current handlerID for this request and updates the status accordingly. If you wish to
   * remove a handler from this request altogether, call this method with the empty string as its
   * parameter.
   *
   * @param handlerID The handlerID to be set.
   */
  public void setHandlerID(@NonNull String handlerID) {
    this.handlerID = handlerID;
    if (handlerID.equals("")) {
      this.status = RequestStatus.UNASSIGNED;
    } else {
      this.status = RequestStatus.PROCESSING;
    }
  }
}
