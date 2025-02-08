package com.kth.aibook.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String FRONT_URL = "http://localhost:5173";

        registry.addMapping("/**")
                .allowedOrigins(FRONT_URL)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
