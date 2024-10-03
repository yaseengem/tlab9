package com.tlab9.live.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Value("${api.key.header}")
    private String apiKeyHeader;

    @Value("${api.key}")
    private String apiKey;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/api/subjects",
            "/modules");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestApiKey = request.getHeader(apiKeyHeader);
        log.info("Request received. API Key Header: {}, Request API Key: {}", apiKeyHeader, requestApiKey);
        log.info("Configured API Key: {}", apiKey);

        String requestPath = request.getRequestURI();
        log.info("Request Path: {}", requestPath);
        if (EXCLUDED_PATHS.stream().anyMatch(requestPath::startsWith)) {
            // Set authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(new ApiKeyAuthenticationToken(requestApiKey, true));
            filterChain.doFilter(request, response);
            return;
        }

        if (apiKey.equals(requestApiKey)) {
            log.info("Allowed request with API Key: {}", requestApiKey);
            // Set authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(new ApiKeyAuthenticationToken(requestApiKey, true));
            filterChain.doFilter(request, response);
        } else {
            log.warn("Unauthorized request with API Key: {}", requestApiKey);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized to access steps");
        }
    }

}