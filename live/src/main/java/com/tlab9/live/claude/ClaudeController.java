package com.tlab9.live.claude;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        JsonNode response = claudeService.getClaudeResponse(prompt);
        return ResponseEntity.ok(response);
    }
}