package com.kth.aibook.controller.oauth;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.dto.member.MemberDto;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.authentication.impl.KakaoAuthenticationService;
import com.kth.aibook.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthorizationController {
    private final KakaoAuthenticationService kakaoAuthenticationService;
    private final MemberService memberService;

    @GetMapping("/login/oauth2/code/kakao")
    public ApiResponse<String> kakaoLogin(@RequestParam("code") String code) {
        String accessToken = kakaoAuthenticationService.getAccessToken(code);
        String email = kakaoAuthenticationService.getEmail(accessToken);
        try {
            MemberDto memberDto = memberService.getMemberByEmail(email);
            //TODO: jwt 발급
            return ApiResponse.success(HttpStatus.OK, "jwt");
        } catch (MemberNotFoundException e) {
            return ApiResponse.success(HttpStatus.FORBIDDEN, email);
            
        }
    }
}
