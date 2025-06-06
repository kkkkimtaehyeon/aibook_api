package com.kth.aibook.common.handler;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.exception.JwtExpiredException;
import com.kth.aibook.dto.common.ApiResponseBody;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(JwtExpiredException.class)
    public ApiResponse<?> handlerJwtExpiredException(JwtException e) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseBody> handlerJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseBody(e.getMessage(), null));
    }


}
