package com.inrix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hits {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("providerHash")
    private String providerHash;

    @JsonProperty("startLoc")
    private String startLoc;

    @JsonProperty("startDateTime")
    private String startDateTime;

    @JsonProperty("endLoc")
    private String endLoc;

    @JsonProperty("endpointType")
    private int endpointType;

    @JsonProperty("tripDistanceMeters")
    private int tripDistanceMeters;

    @JsonProperty("tripMeanSpeedKPH")
    private int tripMeanSpeedKPH;

    @JsonProperty("tripId")
    private String tripId;

    @JsonProperty("endDateTime")
    private String endDateTime;

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("providerType")
    private String providerType;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
