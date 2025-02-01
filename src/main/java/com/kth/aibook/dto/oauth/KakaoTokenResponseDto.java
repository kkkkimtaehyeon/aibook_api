package com.kth.aibook.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponseDto(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") String expiresIn,
        @JsonProperty("scope") String scope,
        @JsonProperty("refresh_token_expires_in") String refreshTokenExpiresIn) {
}