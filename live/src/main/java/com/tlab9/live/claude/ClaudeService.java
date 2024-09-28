package com.tlab9.live.claude;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ClaudeService {

    @Value("${claude.api.url}")
    private String apiUrl;

    @Value("${claude.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ClaudeService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode getClaudeResponse(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");
        headers.set("Content-Type", "application/json");

        Prompt requestBody = new Prompt();
        requestBody.setModel("claude-3-5-sonnet-20240620");
        requestBody.setMax_tokens(20);
        requestBody.setMessages(Collections.singletonList(new Prompt.Message("user", prompt)));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        JsonNode responseJson = objectMapper.readTree(response.getBody());
        JsonNode contentArray = responseJson.get("content");

        if (contentArray != null && contentArray.isArray() && contentArray.size() > 0) {
            return contentArray;
        }

        // Handle the case where the "content" field is missing
        throw new RuntimeException("The response does not contain a 'content' field.");
    }
}