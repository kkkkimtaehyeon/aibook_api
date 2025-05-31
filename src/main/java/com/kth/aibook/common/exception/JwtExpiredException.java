package com.kth.aibook.common.exception;

import io.jsonwebtoken.JwtException;

public class JwtExpiredException extends JwtException {
    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
