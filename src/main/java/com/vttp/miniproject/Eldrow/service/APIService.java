package com.vttp.miniproject.Eldrow.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {
    public static final String API_LINK = "https://random-word-api.vercel.app/api?words=1&length=5&type=uppercase";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getWordFromAPI() {
        ResponseEntity<String[]> response = restTemplate.getForEntity(API_LINK, String[].class);
        String word = response.getBody()[0];
        return word;        
    }
}
