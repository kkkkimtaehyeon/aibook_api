package com.kth.aibook.dto.auth;

public record Tokens(
        String accessToken,
        String refreshToken
) {
}
