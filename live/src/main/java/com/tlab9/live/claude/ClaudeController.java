package com.tlab9.live.claude;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claude")
public class ClaudeController {

    private final ClaudeService claudeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ClaudeController(ClaudeService claudeService, ObjectMapper objectMapper) {
        this.claudeService = claudeService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/chat")
    public ResponseEntity<JsonNode> chat(@RequestBody String requestBody) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        String prompt = jsonNode.get("prompt").asText();

        // Uncomment this line to get the actual response from Claude.
        // JsonNode response = claudeService.getClaudeResponse(prompt);

        // Dummy response. Return this in testing. 
        ArrayNode response = objectMapper.createArrayNode();
        ObjectNode responseObject = objectMapper.createObjectNode();
        responseObject.put("type", "text");
        responseObject.put("text", "I am dummy response. Later this can be lot more..");
        responseObject.put("prompt", prompt); // Add the prompt to the response
        response.add(responseObject);

        return ResponseEntity.ok(response);
    }
}