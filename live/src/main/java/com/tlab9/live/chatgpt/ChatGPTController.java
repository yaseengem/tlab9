// package com.tlab9.live.chatgpt;

// import com.tlab9.live.dto.ChatGPTRequest;
// import com.tlab9.live.dto.ChatGPTResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/chatgpt")
// public class ChatGPTController {

//     @Autowired
//     private ChatGPTService chatGPTService;

//     @PostMapping
//     public ResponseEntity<ChatGPTResponse> askChatGPT(@RequestBody ChatGPTRequest request) {
//         ChatGPTResponse response = chatGPTService.askChatGPT(request);
//         return ResponseEntity.ok(response);
//     }
// }