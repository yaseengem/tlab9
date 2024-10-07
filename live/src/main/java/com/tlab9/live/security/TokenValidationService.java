
package com.tlab9.live.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
public class TokenValidationService {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationService.class);

    private final RestTemplate restTemplate;
    private final String clientId;

    public TokenValidationService(RestTemplate restTemplate, @Value("${google.client.id}") String clientId) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
    }

    @Cacheable("tokenValidation")
    public Map<String, Object> validateToken(String token) {
        logger.info("Cache miss for token: {}", token);
        logger.info("Validating access token: {}", token);
        try {
            String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + token;
            logger.debug("Requesting token info from URL: {}", url);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> tokenInfo = response.getBody();
            if (tokenInfo != null) {
                logger.debug("Token info received: {}", tokenInfo);
                String audience = (String) tokenInfo.get("aud");
                if (clientId.equals(audience)) {
                    logger.info("Access token is valid for client ID: {}", clientId);
                    return tokenInfo;
                } else {
                    logger.warn("Access token audience mismatch. Expected: {}, Actual: {}", clientId, audience);
                }
            } else {
                logger.warn("Access token info is null");
            }
        } catch (HttpClientErrorException e) {
            logger.error("Invalid access token: {}", e.getMessage());
        }
        return null;
    }
}