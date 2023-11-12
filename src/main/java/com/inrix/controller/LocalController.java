package com.inrix.controller;

import com.inrix.model.Restaurant;
import com.inrix.model.RestaurantResponse;
import com.inrix.model.Scores;
import com.inrix.repositories.RestaurantRepository;
import com.inrix.repositories.ScoreRepository;
import com.inrix.utils.BoundingBoxCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class LocalController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    private final BoundingBoxCalculator boundingBoxCalculator = new BoundingBoxCalculator();

    @GetMapping("/getRestaurantsData")
    public List<RestaurantResponse> getRestaurantsData() {

        List<RestaurantResponse> restaurantResponses = new ArrayList<>();
       List<Restaurant> restaurantList =  restaurantRepository.findAll();
       for(Restaurant x: restaurantList){
           RestaurantResponse restaurantResponse = new RestaurantResponse();
           restaurantResponse.setRating(x.getRating());
           restaurantResponse.setTypes(x.getTypes());
           restaurantResponse.setVicinity(x.getVicinity());
           restaurantResponse.setLng(x.getLng());
           restaurantResponse.setLat(x.getLat());
           restaurantResponse.setId(x.getId());
           restaurantResponse.setName(x.getName());
           restaurantResponse.setUrl(x.getUrl());
           try {
               Scores scores = scoreRepository.findById(x.getId()).get();
               restaurantResponse.setComposite_score(scores.getComposite_score());
               restaurantResponse.setDistance_score(scores.getDistance_score());
               restaurantResponse.setFrequency_score(scores.getFrequency_score());
               restaurantResponse.setRecency_score(scores.getRecency_score());
               restaurantResponse.setTraffic_score(scores.getTraffic_score());
           }catch (Exception e){
               continue;
           }
           restaurantResponses.add(restaurantResponse);
       }

       restaurantResponses.sort(new Comparator<RestaurantResponse>() {
           @Override
           public int compare(RestaurantResponse o1, RestaurantResponse o2) {
               if(o1.getFrequency_score() > o2.getFrequency_score())
                   return -1;
               else if(o1.getFrequency_score() < o2.getFrequency_score())
                   return 1;
               else
                   return 0;
           }
       });
       return restaurantResponses;
    }
}
