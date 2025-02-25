package com.tlab9.live.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Autowired
    private GoogleTokenFilter googleTokenFilter;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // Specify exact origins
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedOrigin("http://localhost:8082");
        config.addAllowedOrigin("http://localhost:8083");
        config.addAllowedOrigin("http://localhost:8084");
        config.addAllowedOrigin("https://localhost:3000");
        config.addAllowedOrigin("https://localhost:8080");
        config.addAllowedOrigin("https://localhost:8081");
        config.addAllowedOrigin("https://localhost:8082");
        config.addAllowedOrigin("https://localhost:8083");
        config.addAllowedOrigin("https://localhost:8084");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // .requestMatchers("/swagger-ui/**").permitAll()
                        // .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(googleTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Unauthorized request - {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Access denied - {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        }))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // Apply the CORS configuration

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // HTTP origin
        config.addAllowedOrigin("http://localhost:8080"); // HTTP origin
        config.addAllowedOrigin("http://localhost:8081"); // HTTP origin
        config.addAllowedOrigin("http://localhost:8082"); // HTTP origin
        config.addAllowedOrigin("http://localhost:8083"); // HTTP origin
        config.addAllowedOrigin("http://localhost:8084"); // HTTP origin
        config.addAllowedOrigin("https://localhost:3000"); // HTTPS origin
        config.addAllowedOrigin("https://localhost:8080"); // HTTPS origin
        config.addAllowedOrigin("https://localhost:8081"); // HTTPS origin
        config.addAllowedOrigin("https://localhost:8082"); // HTTPS origin
        config.addAllowedOrigin("https://localhost:8083"); // HTTPS origin
        config.addAllowedOrigin("https://localhost:8084"); // HTTPS origin
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer() {
    // return (web) -> web.ignoring().requestMatchers("/public/**");
    // }
}