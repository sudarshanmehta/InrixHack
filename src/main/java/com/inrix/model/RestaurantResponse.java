package com.inrix.model;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RestaurantResponse {
    private Long id;
    private Double lat;
    private Double lng;
    private String vicinity;
    private String name;
    private Double rating;
    private String types;
    private String url;
    private double recency_score;
    private double frequency_score;
    private double distance_score;
    private double traffic_score;
    private double composite_score;
}
