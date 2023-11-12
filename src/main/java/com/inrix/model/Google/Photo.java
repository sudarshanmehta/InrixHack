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
public class Photo {
    @JsonProperty("height")
    private int height;

    @JsonProperty("html_attributions")
    private List<String> htmlAttributions;

    @JsonProperty("photo_reference")
    private String photoReference;

    @JsonProperty("width")
    private int width;
}

