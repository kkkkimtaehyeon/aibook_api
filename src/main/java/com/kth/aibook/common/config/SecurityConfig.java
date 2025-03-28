package com.kth.aibook.common.config;


import com.kth.aibook.common.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        // crsf 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(cors -> cors.configure(http));

        http.authorizeHttpRequests(path -> path
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() //preFlight를 위한 option 허용
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/ad").permitAll()
                .requestMatchers(HttpMethod.GET, "/notion").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers("/login/**", "/oauth2/authorization/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/members").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/stories/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/stories/*/voices/*/dubbing/completed").permitAll()
                .anyRequest().authenticated()
        );
        return http.build();
    }
}



