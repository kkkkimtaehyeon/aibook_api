package com.kth.aibook.common.exception;

public class AuthenticationFailException extends RuntimeException{
    public AuthenticationFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
