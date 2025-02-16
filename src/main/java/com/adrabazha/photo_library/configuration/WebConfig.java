package com.adrabazha.photo_library.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${cors.methods}")
    private String[] allowedMethods;

    @Value("${cors.headers}")
    private String[] allowedHeaders;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(frontendUrl)
                        .allowedMethods(allowedMethods)
                        .exposedHeaders(allowedHeaders)
                        .allowCredentials(true);
            }
        };
    }
}
