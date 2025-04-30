package com.kth.aibook.common.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.provider.JwtProvider;
import com.kth.aibook.service.authentication.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_KEY = "Authorization";
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // header에 토큰이 있으면 SecurityContext에 등록
        try {
            String token = request.getHeader(AUTHORIZATION_KEY);
            if (token != null && !token.isBlank()) {
                token = extractToken(request.getHeader(AUTHORIZATION_KEY));
                Authentication authentication = jwtProvider.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            sendErrorResponse(response, e);
        }
    }

    private String extractToken(String token) {
        return token.substring(7);
    }

    private void sendErrorResponse(HttpServletResponse response, Exception exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<?> apiResponse = ApiResponse.error(HttpStatus.UNAUTHORIZED, exception.getMessage());
        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
