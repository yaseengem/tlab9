package com.tlab9.live.claude;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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

        log.info("Entering getClaudeResponse method with prompt: {}", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");
        headers.set("Content-Type", "application/json");

        Prompt requestBody = new Prompt();
        requestBody.setModel("claude-3-5-sonnet-20240620");
        requestBody.setMax_tokens(100);
        requestBody.setTemperature(0.7); // Set the temperature here
        requestBody.setMessages(Collections.singletonList(new Prompt.Message("user", prompt)));

        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        log.info("Constructed JSON request body: {}", jsonRequestBody);

        HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);
        log.info("Sending request to Claude API at URL: {}", apiUrl);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        log.info("Received response from Claude API with status code: {}", response.getStatusCode());

        JsonNode responseJson = objectMapper.readTree(response.getBody());
        log.info("Received JSON response: {}", responseJson);

        if (responseJson.has("content") && responseJson.get("content").isArray()) {
            JsonNode contentArray = responseJson.get("content");
            if (contentArray.size() > 0 && contentArray.get(0).has("text")) {
                String text = contentArray.get(0).get("text").asText();
                ObjectNode result = objectMapper.createObjectNode();
                result.put("text", text);

                log.info("Text result: {}", result);
                return result;
            }
        }

        // Handle the case where the "content" field is missing
        log.error("The response does not contain a 'content' field or 'text' field.");
        throw new RuntimeException("The response does not contain a 'content' field or 'text' field.");
    }
}