package com.kth.aibook.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String CLIENT_SERVER = "http://localhost:5173";
            private static final String AI_SERVER = "http://localhost:8000";
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(CLIENT_SERVER, AI_SERVER)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTION")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
