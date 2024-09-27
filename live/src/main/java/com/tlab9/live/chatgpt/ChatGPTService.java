// package com.tlab9.live.chatgpt;

// import com.tlab9.live.dto.ChatGPTRequest;
// import com.tlab9.live.dto.ChatGPTResponse;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.client.WebClientResponseException;

// import reactor.core.publisher.Mono;

// @Service
// @Slf4j
// public class ChatGPTService {

//     @Value("${chatgpt.api.url}")
//     private String apiUrl;

//     @Value("${chatgpt.api.key}")
//     private String apiKey;

//     private final WebClient webClient;

//     public ChatGPTService(WebClient.Builder webClientBuilder) {
//         this.webClient = webClientBuilder.baseUrl(apiUrl).build();
//     }

//     public ChatGPTResponse askChatGPT(ChatGPTRequest request) {
//         log.info("Entering askChat method with question: " + request.getQuestion());
//         try {
//             Mono<ChatGPTResponse> response = webClient.post()
//                     .uri("/v1/chat/completions")
//                     .header("Authorization", "Bearer " + apiKey)
//                     .header("Content-Type", "application/json")
//                     .bodyValue(buildRequestBody(request.getQuestion()))
//                     .retrieve()
//                     .bodyToMono(ChatGPTResponse.class);

//             ChatGPTResponse chatGPTResponse = response.block();
//             if (chatGPTResponse != null && !chatGPTResponse.getChoices().isEmpty()) {
//                 String messageContent = chatGPTResponse.getChoices().get(0).getMessage().getContent();
//                 log.info("Response from chatgpt was: " + messageContent);
//                 System.out.println("Response from chatgpt was: " + messageContent);
//             } else {
//                 log.warn("No response received from chatgpt.");
//                 System.out.println("No response received from chatgpt.");
//             }

//             return chatGPTResponse;
//         } catch (WebClientResponseException e) {
//             log.error("Error response from chatgpt: " + e.getResponseBodyAsString());
//             System.out.println("Error response from chatgpt: " + e.getResponseBodyAsString());
//             return null;
//         } catch (Exception e) {
//             log.error("Exception occurred while calling chatgpt: " + e.getMessage());
//             System.out.println("Exception occurred while calling chatgpt: " + e.getMessage());
//             return null;
//         }
//     }

//     private String buildRequestBody(String question) {
//         return "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + question + "\"}], \"max_tokens\": 150 }";
//     }
// }