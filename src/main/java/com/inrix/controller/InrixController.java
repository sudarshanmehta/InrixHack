package com.inrix.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inrix.model.Hits;
import com.inrix.model.HitsResponse;
import com.inrix.model.Restaurant;
import com.inrix.repositories.HitRepository;
import com.inrix.repositories.RestaurantRepository;
import com.inrix.utils.BoundingBoxCalculator;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
public class InrixController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private HitRepository hitRepository;

    private static final String INRIX_API_BASE_URL = "https://api.iq.inrix.com/v1/trips";

    // Set your initial token and token expiration time (in milliseconds) here
    private String BEARER_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhcHBJZCI6InJscWVmYTZsazEiLCJ0b2tlbiI6eyJpdiI6IjcxMDU4ZjQ2YjhhZDNiZGJhM2U0NDAxYTU4NjY5MTFjIiwiY29udGVudCI6Ijk2YmViMjEyNjkzZGQwZDc0NDQzMTgxMWViNDA1MzFhNjgyZGIwMzVkN2U0NDc5YTRkMDZiN2I5OWQ4YWFmZGY5OGU3YzMxZjEzZDZjZmQyYjhjZWI0OWJlZjljMWJlNGM5ZDBhOTA2ZDAzYzUwMjRlNTRmZGY5OTk4MGViNjJhNzMyZjNhZmI1MWQ4ZWUwZWQxZmRkMzI5ZGJiM2E0YjgzY2MyZmEwMWU2MjU2YTI1MTI3NzQyZDZlMmMzMWI4MzlhNzNkNmRiOGUyZWIxN2NjYjRjMjRjZGFlZmM3NDhmNGU0OWNhZmJmMTZhZTBmMjE4NGQ1Nzc1NzBjNGQzMzUzZWUwOTNhODA1MTQ4NzA2M2Y3NTZiYzhmMjkyYzQ5M2I5NWQ5ZjA1YzhmMGZhMzY2ZGI3NTI3YzdlZjI4MWRjMDYyNjUwOTI0NDIyODY4NWJlM2ExOTI3NDVkMTMwNGYxZThjZDZmOWFkZjczMDJkM2I0MTc5YjJhZjM4MDk1M2Y4OTkwZDBiNmM3NGJkOWJlNzMzZTUwYzIwZDhiOTVhNTA1YmE4MGY3YjMxODk5NGQ2YTEwMzNiODQyMjMxNzYzZGY0Yjg3ODU1MDRiYjVmNzY0NmUyNWVkNWRkNzQ2ZTcyZjg2ZGQ5M2UzYzBkZjAxMmU4NjE3MmVmYWE0Njk3MjczYTFjMjQ2NzA3MGE3MmRjZTY2MTgyODU1NjRlOGZmZjE5NThhMmVmZThlYjhjYWJkYmYyYzQzODBhYjc0MzIyODgwMjA3ZTRlMmIyNGFkNmNmZmVjYWYzMjc3Nzc1ZGY0ZGFkNTExMWQ1YzMyZDM5OTQzNDFjZDdiMWEyZTQxMTg2YWRiMTBjODg2MzJlY2I1OTRhIn0sInNlY3VyaXR5VG9rZW4iOnsiaXYiOiI3MTA1OGY0NmI4YWQzYmRiYTNlNDQwMWE1ODY2OTExYyIsImNvbnRlbnQiOiJhOTlmODkzNTY5MWZkZGM5NDQ2OTBiNmZjNzVlNmU3MzQ3MDhjYjIzZjRjNjQ3Zjc0NDZmYmFmZDhkZDBiYmRmOTQ5NmMzNTU0ZGRjZDRlMGU1ZDk5YmE1In0sImp0aSI6IjhmYTE4YzgzLTNmMzctNDIzOC1hODgxLThiZTM3OGQ5YzA1OSIsImlhdCI6MTY5OTcyOTM1MywiZXhwIjoxNjk5NzMyOTUzfQ.p6NWLumaEtmrmM5crqTtCLHZdj7EomX_YQeIK1PJ1l4";

    private final TokenController tokenController;

    private final BoundingBoxCalculator boundingBoxCalculator = new BoundingBoxCalculator();

    @Autowired
    public InrixController(TokenController tokenController) {
        this.tokenController = tokenController;
    }

    @GetMapping("/getTradeData")
    public String getTradeData(
            @RequestParam(name = "geoFilterType", defaultValue = "bbox") String geoFilterType,
            @RequestParam(name = "od", defaultValue = "destination") String od,
            @RequestParam(name = "limit", defaultValue = "100") int limit,
            @RequestParam(name = "points", defaultValue = "37.734622|-122.471603,37.743627|-122.463850,37.743066|-122.475429") String points,
            @RequestParam(name = "startDateTime", defaultValue = ">=2023-06-01T02:31") String startDateTime,
            @RequestParam(name = "endDateTime", defaultValue = "<=2023-06-15T02:31") String endDateTime) throws JsonProcessingException {

        BEARER_TOKEN = "Bearer " + tokenController.refreshAppToken().getBody().getResult().getToken();

        List<Restaurant> restaurantList = restaurantRepository.findAll();

        for(Restaurant restaurant : restaurantList) {

            double lat1 = DoubleRounder.round(restaurant.getLat(), 6);
            double long1 = DoubleRounder.round(restaurant.getLng(), 6);

            double[] arr = boundingBoxCalculator.calculateBoundingBox(lat1, long1, 100, 100);

            points = arr[0] + "|" + arr[1] + "," + arr[2] + "|" + arr[3];

            RestTemplate restTemplate = new RestTemplate();

            String apiUrl = INRIX_API_BASE_URL + "?" +
                    "geoFilterType=" + geoFilterType + "&" +
                    "od=" + od + "&" +
                    "limit=" + limit + "&" +
                    "points=" + points + "&" +
                    "startDateTime=" + startDateTime + "&" +
                    "endDateTime=" + endDateTime;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", BEARER_TOKEN);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> jsonResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            HitsResponse response = objectMapper.readValue(jsonResponse.getBody(), HitsResponse.class);

           for (Hits hit : response.getHits()){
                hit.setRestaurant(restaurant);
                hitRepository.saveAndFlush(hit);
           }
        }

        return "OK";
    }
}
