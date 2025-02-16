package com.tlab9.live.claude;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@Slf4j
public class ClaudeController {

    private final ClaudeService claudeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ClaudeController(ClaudeService claudeService, ObjectMapper objectMapper) {
        this.claudeService = claudeService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<JsonNode> chat(@RequestBody String requestBody) throws Exception {
        log.info("Entering chat method with requestBody: {}", requestBody);

        JsonNode jsonNode = objectMapper.readTree(requestBody);
        String prompt = jsonNode.get("prompt").asText();
        log.info("Extracted prompt: {}", prompt);

        // Uncomment this line to get the actual response from Claude.
        JsonNode response = claudeService.getClaudeResponse(prompt);
        log.info("Received response from Claude service", response);


        //Uncomment this block to get dummy response.
        // ArrayNode response = objectMapper.createArrayNode();
        // ObjectNode responseObject = objectMapper.createObjectNode();
        // responseObject.put("type", "text");
        // responseObject.put("text", "I am dummy response. Later this can be lot more..");
        // responseObject.put("prompt", prompt); // Add the prompt to the response
        // response.add(responseObject);

        return ResponseEntity.ok(response);
    }
}