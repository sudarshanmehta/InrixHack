package com.inrix.controller;

import com.inrix.model.TokenResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequestMapping("/token")
public class TokenController {

    HashMap<String,TokenResponse> repo = new HashMap<>();
    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAppToken() {
        // Define the API URL
        String apiUrl = "https://api.iq.inrix.com/auth/v1/appToken?appId=rlqefa6lk1&hashToken=cmxxZWZhNmxrMXxZbGhoWHZNV0wwMUdpWVdOR0BHNmphejRqcDhPN1UyMjZFblhudkgx";

        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Create headers with the content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a request entity with the URL and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send a GET request to the API URL
        ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, TokenResponse.class);

        // Check the HTTP status code in the response
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Return the parsed TokenResponse object
            repo.put("s1",responseEntity.getBody());
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        } else {
            // Token refresh failed
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenResponse getSavedToken(String s1){
        return repo.get(s1);
    }
}
