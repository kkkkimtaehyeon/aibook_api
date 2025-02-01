package com.kth.aibook.client;

import com.kth.aibook.dto.oauth.KakaoTokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakaoAuthClient", url = "https://kauth.kakao.com")
public interface KakaoAuthenticationClient {

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponseDto getToken(@RequestParam(name = "grant_type") String grantType,
                                   @RequestParam(name = "client_id") String clientId,
                                   @RequestParam(name = "redirect_uri") String redirectUri,
                                   @RequestParam(name = "code") String code,
                                   @RequestParam(name = "client_secret") String clientSecret);

}
