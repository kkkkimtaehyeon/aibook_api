package com.kth.aibook.controller.auth;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.common.provider.JwtProvider;
import com.kth.aibook.dto.member.MemberDto;
import com.kth.aibook.dto.member.MemberSimpleDto;
import com.kth.aibook.dto.oauth.KakaoOauthProviderDto;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.authentication.impl.KakaoAuthenticationService;
import com.kth.aibook.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthorizationController {
    private final KakaoAuthenticationService kakaoAuthenticationService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    // oauth 로그인
    @GetMapping("/login/oauth2/code/kakao")
    public ApiResponse<Object> kakaoLogin(@RequestParam("code") String code) {
        String accessToken = kakaoAuthenticationService.getAccessToken(code);
        long oauthProviderMemberId = kakaoAuthenticationService.getId(accessToken);
        try {
            MemberDto memberDto = memberService.getMemberByOauthMemberId("kakao", oauthProviderMemberId);

            String token = jwtProvider.generateAccessToken(memberDto);
            return ApiResponse.success(HttpStatus.OK, token);
        } catch (MemberNotFoundException e) {
            return ApiResponse.success(HttpStatus.FORBIDDEN, new KakaoOauthProviderDto("kakao", oauthProviderMemberId));
        }
    }

    // 회원정보
    @GetMapping("/api/me")
    public ApiResponse<MemberSimpleDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
        if (userDetail == null) {
            throw new MemberNotFoundException("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
        Long memberId = Long.valueOf(userDetail.getUsername());
        MemberSimpleDto memberSimpleDto = memberService.getMemberSimpleInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberSimpleDto);
    }

    @GetMapping("/api/logout")
    public ApiResponse<?> logout() {
        SecurityContextHolder.clearContext();
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
