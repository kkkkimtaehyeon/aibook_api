package com.kth.aibook.common.handler;

import com.kth.aibook.common.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ApiResponse<?> handlerJwtException(JwtException e) {
        return ApiResponse.error(HttpStatus.FORBIDDEN, e.getMessage());
    }


}
