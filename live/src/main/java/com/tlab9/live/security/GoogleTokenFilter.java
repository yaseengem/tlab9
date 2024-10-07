package com.tlab9.live.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

    private final TokenValidationService tokenValidationService;

    @Value("${google.client.id}")
    private String clientId;

    public GoogleTokenFilter(TokenValidationService tokenValidationService) {
        this.tokenValidationService = tokenValidationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Check if the request is already authenticated (skip if true)
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            Map<String, Object> tokenInfo = tokenValidationService.validateToken(token);
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
                logger.warn("Invalid access token: validation failed.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        else {
            logger.warn("Invalid access token: missing or invalid format.");
        }
        filterChain.doFilter(request, response);
    }
}