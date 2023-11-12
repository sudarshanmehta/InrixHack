package com.inrix.model.Google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceLocation {
    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lng")
    private double longitude;
}
