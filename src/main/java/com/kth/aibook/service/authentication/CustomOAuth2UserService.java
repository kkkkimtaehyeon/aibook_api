package com.kth.aibook.service.authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 카카오 사용자 정보 추출 및 가공
        Map<String, Object> kakaoAccount =
                (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        Map<String, Object> profile =
                (Map<String, Object>) kakaoAccount.get("profile");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("nickname", profile.get("nickname"));
        attributes.put("email", kakaoAccount.get("email"));
        attributes.put("id", oAuth2User.getAttribute("id"));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("MEMBER")),
                attributes,
                "email"
        );
    }
}