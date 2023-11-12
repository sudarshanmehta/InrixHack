package com.inrix.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Trade {
    String providerHash;
    String startLoc;
    String startDateTime;
    String endLoc;
    String tripDistanceMeters;
    String tripMeanSpeedKPH;
    String tripId;
    String endDateTime;
    String deviceId;
    String providerType;
    String endpointType;
}
