package com.adrabazha.photo_library.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> {
            request.anyRequest().authenticated();
        });

        http.oauth2Login(oauth2 ->
            oauth2.defaultSuccessUrl(frontendUrl, true)
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.setBuilder(http));

        return http.build();
    }
}
