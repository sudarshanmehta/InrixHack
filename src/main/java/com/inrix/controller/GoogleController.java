package com.inrix.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inrix.model.Google.GooglePlace;
import com.inrix.model.Google.GooglePlacesResponse;
import com.inrix.model.Restaurant;
import com.inrix.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class GoogleController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final String googleApiUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    private final String apiKey = "AIzaSyBGy1UTVux_0QX-JSivKUWREuQsB4jAvwc";

    @GetMapping("/restaurants")
    public List<GooglePlace> getNearbyRestaurants(
            @RequestParam(name = "location", defaultValue = "37.8003359,-122.4090227") String location,
            @RequestParam(name = "radius", defaultValue = "5000") String radius,
            @RequestParam(name = "type", defaultValue = "restaurant") String type
    ) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String url = googleApiUrl + "?location=" + location + "&radius=" + radius + "&type=" + type + "&key=" + apiKey;

        String jsonResponse = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        GooglePlacesResponse response = objectMapper.readValue(jsonResponse, GooglePlacesResponse.class);

        List<GooglePlace> places = response.getResults();

        // Save the retrieved places to the repository as Restaurant entities

        for (GooglePlace place : places) {
            Restaurant restaurant = new Restaurant();
            restaurant.setLat(place.getGeometry().getLocation().getLatitude());
            restaurant.setLng(place.getGeometry().getLocation().getLongitude());
            restaurant.setVicinity(place.getVicinity());
            restaurant.setName(place.getName());
            restaurant.setRating(place.getRating());
            restaurant.setTypes(String.join(", ", place.getTypes()));
            place.getPhoto().stream().forEach(x->{
                for(String imageUrl : x.getHtmlAttributions()) {
                    restaurant.setUrl(parseHrefLink(imageUrl));
                }
            });
            restaurantRepository.saveAndFlush(restaurant);
        }


        return places;
    }


    public static String parseHrefLink(String html) {
        // Define a regular expression to match links
        Pattern pattern = Pattern.compile("<a\\s+href=\"(.*?)\".*?>");

        Matcher matcher = pattern.matcher(html);
        String link = "";
        while (matcher.find()) {
            link = matcher.group(1);
        }
        return link;
    }
}
