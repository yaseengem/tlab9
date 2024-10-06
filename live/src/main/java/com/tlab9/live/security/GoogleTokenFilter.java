package com.tlab9.live.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class GoogleTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(GoogleTokenFilter.class);

    @Value("${google.client.id}")
    private String clientId;

    private final RestTemplate restTemplate;

    public GoogleTokenFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            logger.info("Authorization header found with Bearer token.");
            accessToken = accessToken.substring(7);

            try {
                String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?access_token=" + accessToken;
                ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                        tokenInfoUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Map<String, Object>>() {}
                );
                Map<String, Object> tokenInfo = responseEntity.getBody();

                if (tokenInfo != null) {
                    String audience = (String) tokenInfo.get("aud");
                    if (clientId.equals(audience)) {
                        String userId = (String) tokenInfo.get("sub");
                        logger.info("User ID: " + userId);
                        logger.info("Token verified successfully.");

                        // Create a UserDetails object
                        UserDetails userDetails = User.withUsername(userId)
                                .password("") // No password needed for token authentication
                                .authorities(Collections.emptyList()) // Set authorities if needed
                                .build();

                        // Create an Authentication object
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Set the authentication in the SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        logger.warn("Invalid access token: audience mismatch.");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                } else {
                    logger.warn("Invalid access token: token info is null.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (HttpClientErrorException e) {
                logger.error("Token verification failed: {}", e.getStatusCode());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                logger.error("Token verification failed.", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            logger.warn("Authorization header is missing or does not contain a Bearer token.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        // Continue the request processing regardless of token verification result
        filterChain.doFilter(request, response);
    }
}