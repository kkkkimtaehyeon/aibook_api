package com.kth.aibook.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final HttpStatus code;
    private final T data;
    private final String message;

    public ApiResponse(boolean success, HttpStatus httpStatus, String message, T data) {
        this.success = success;
        this.code = httpStatus;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, T data) {
        return new ApiResponse<T>(true, httpStatus, null, data);
    }

    public static <T> ApiResponse<T> fail(HttpStatus httpStatus, String message) {
        return new ApiResponse<T>(false, httpStatus, message, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
        return new ApiResponse<T>(false, httpStatus, message, null);
    }
}