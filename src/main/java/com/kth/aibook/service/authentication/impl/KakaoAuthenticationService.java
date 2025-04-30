package com.kth.aibook.service.authentication.impl;

import com.kth.aibook.client.KakaoAuthenticationClient;
import com.kth.aibook.client.KakaoInfoClient;
import com.kth.aibook.common.exception.KakaoOauthException;
import com.kth.aibook.dto.oauth.KakaoTokenResponseDto;
import com.kth.aibook.dto.oauth.KakaoUserInfoResponseDto;
import com.kth.aibook.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KakaoAuthenticationService implements AuthenticationService {
    private final KakaoAuthenticationClient kakaoAuthenticationClient;
    private final KakaoInfoClient kakaoInfoClient;

    @Value("${oauth.kakao.grant-type}")
    private String grantType;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private static final String BEARER = "Bearer ";


    @Override
    public String getAccessToken(String code) {
        KakaoTokenResponseDto tokenResponse = kakaoAuthenticationClient.getToken(grantType, clientId, redirectUri, code, clientSecret);
        if (tokenResponse == null) {
            throw new KakaoOauthException("카카오 액세스 토큰 발급이 실패했습니다.");
        }
        return tokenResponse.accessToken();
    }

    @Override
    public String getEmail(String accessToken) {
        KakaoUserInfoResponseDto userInfo = kakaoInfoClient.getInfo(BEARER + accessToken);
        if (userInfo == null) {
            throw new KakaoOauthException("카카오 유저 정보를 가져오는데 실패했습니다.");
        }
        return userInfo.kakao_account().email();
    }

    @Override
    public long getUserIdFromToken(String accessToken) {
        KakaoUserInfoResponseDto userInfo = kakaoInfoClient.getInfo(BEARER + accessToken);
        if (userInfo == null) {
            throw new KakaoOauthException("카카오 유저 정보를 가져오는데 실패했습니다.");
        }
        return userInfo.id();
    }
}
