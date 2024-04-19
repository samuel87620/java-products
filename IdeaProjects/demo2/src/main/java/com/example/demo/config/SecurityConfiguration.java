package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Configure paths to permit all without authentication
                        .requestMatchers("/api/users/**", "/users/**", "/user/**", "/css/**", "/favicon.ico", "/js/**", "/img/**", "/webjars/**").permitAll()
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic authentication
                .httpBasic(httpBasic -> httpBasic.realmName("YourAppRealm"));
        return http.build();
    }
}
