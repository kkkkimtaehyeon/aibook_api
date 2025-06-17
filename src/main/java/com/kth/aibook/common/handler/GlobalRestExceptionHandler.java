package com.kth.aibook.common.handler;

import com.kth.aibook.common.exception.JwtExpiredException;
import com.kth.aibook.dto.common.ApiResponseBody;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ApiResponseBody> handleJwtExpiredException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseBody(e.getMessage(), null));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseBody> handleJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseBody(e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorDetailMessage = new StringBuilder();
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        for (ObjectError error : errors) {
            errorDetailMessage.append(error.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseBody(errorDetailMessage.toString(), null));
    }
}
