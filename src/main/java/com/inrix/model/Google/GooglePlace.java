package com.inrix.model.Google;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlace {
    @JsonProperty("name")
    private String name;

    @JsonProperty("geometry")
    private GooglePlaceGeometry geometry;

    @JsonProperty("vicinity")
    private String vicinity;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("photos")
    private List<Photo> photo;

    @JsonProperty("types")
    private List<String> types;
}


