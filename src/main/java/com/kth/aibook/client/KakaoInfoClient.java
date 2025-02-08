package com.kth.aibook.client;

import com.kth.aibook.dto.oauth.KakaoUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoInfoClient", url = "https://kapi.kakao.com/v2")
public interface KakaoInfoClient {

    @GetMapping(value = "/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserInfoResponseDto getInfo(@RequestHeader(name = "Authorization") String bearerToken);
}
