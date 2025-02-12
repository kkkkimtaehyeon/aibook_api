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
                .requestMatchers("/login/**", "/oauth2/authorization/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/members").permitAll()
                .anyRequest().authenticated()
        );

        return http.build();
    }
}



