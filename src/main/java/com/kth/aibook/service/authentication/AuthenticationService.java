package com.kth.aibook.service.authentication;

public interface AuthenticationService {
    String getAccessToken(String code);
    String getEmail(String accessToken);
    long getUserIdFromToken(String accessToken);
}
