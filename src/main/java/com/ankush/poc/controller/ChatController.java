package com.ankush.poc.controller;

import com.ankush.poc.payload.ChatRequest;
import com.ankush.poc.payload.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ChatController {

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String chat(@RequestParam String prompt) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);
        HttpEntity<ChatRequest> chatHttprequest = new HttpEntity<>(request);
        // call the API
//        ResponseEntity<ChatResponse> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, chatHttprequest, ChatResponse.class);
//         ChatResponse response = responseEntity.getBody();
          request.setTemperature(1);
          request.setMax_tokens(256);
          request.setTop_p(1);
          request.setFrequency_penalty(0);
          request.setPresence_penalty(0);
        ChatResponse response = restTemplate.postForObject(apiUrl,request, ChatResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        // return the first response
        return response.getChoices().get(0).getMessage().getContent().toString();
    }
}
