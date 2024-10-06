package com.tlab9.live.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Autowired
    private ApiKeyFilter apiKeyFilter;


    @Autowired
    private GoogleTokenFilter googleTokenFilter;

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http
    // .authorizeHttpRequests(authz -> authz
    // .requestMatchers(
    // "/v3/api-docs",
    // "/swagger-ui/**"
    // ).permitAll()
    // .anyRequest().authenticated()
    // )
    // .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
    // .exceptionHandling(exceptionHandling -> exceptionHandling
    // .authenticationEntryPoint((request, response, authException) ->
    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
    // )
    // .csrf(csrf -> csrf.disable());

    // return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // .requestMatchers("/api/subjects").permitAll()
                        // .requestMatchers("/api/subjects/**").permitAll()
                        // .requestMatchers("/swagger-ui.html").permitAll()
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
                        }));
        return http.build();
    }

    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer() {
    //     return (web) -> web.ignoring().requestMatchers("/public/**");
    // }
}