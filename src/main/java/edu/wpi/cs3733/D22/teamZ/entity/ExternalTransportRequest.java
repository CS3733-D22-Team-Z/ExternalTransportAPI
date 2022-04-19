package edu.wpi.cs3733.D22.teamZ.entity;

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
}
