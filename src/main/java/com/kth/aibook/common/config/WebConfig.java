package com.kth.aibook.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${front-domain}")
    private String FRONT_DOMAIN;
    private static final String AI_SERVER = "http://localhost:8000";

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5174","http://localhost:5173", "http://localhost:8000", FRONT_DOMAIN)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTION")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
