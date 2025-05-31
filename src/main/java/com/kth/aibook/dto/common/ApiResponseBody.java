package com.kth.aibook.dto.common;

public record ApiResponseBody(String message, Object data) {
    public ApiResponseBody(Object data) {
        this(null, data);
    }
}
