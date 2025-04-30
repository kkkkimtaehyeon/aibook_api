package com.kth.aibook.controller.auth;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.auth.TokenRequestDto;
import com.kth.aibook.dto.auth.Tokens;
import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.dto.oauth.KakaoOauthProviderDto;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.authentication.TokenService;
import com.kth.aibook.service.authentication.impl.KakaoAuthenticationService;
import com.kth.aibook.service.member.MemberService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final KakaoAuthenticationService kakaoAuthenticationService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @GetMapping("/health")
    public String healthCheck() {
        return "healthy";
    }

    /**
     * oauth 로그인
     *
     * @param code
     * @param res
     * @return
     */
    @GetMapping("/login/oauth2/code/kakao")
    public ApiResponse<Object> kakaoLogin(@RequestParam("code") String code, HttpServletResponse res) {
        String kakaoToken = kakaoAuthenticationService.getAccessToken(code);
        long oauthProviderMemberId = kakaoAuthenticationService.getUserIdFromToken(kakaoToken);
        try {
            MemberDetailDto memberDetailDto = memberService.getMemberByOauthMemberId("kakao", oauthProviderMemberId);
            TokenRequestDto tokenRequestDto = new TokenRequestDto(memberDetailDto);
            Tokens tokens = tokenService.issueTokens(tokenRequestDto);
            // 쿠키에 refresh token 저장
            addRefreshTokenCookie(tokens.refreshToken(), res);
            return ApiResponse.success(HttpStatus.OK, tokens.accessToken());
        } catch (MemberNotFoundException e) {
            return ApiResponse.success(HttpStatus.FORBIDDEN, new KakaoOauthProviderDto("kakao", oauthProviderMemberId));
        }
    }

    /**
     * 토큰 재발급
     *
     * @param req
     * @param res
     * @return
     */
    @PostMapping("/api/token/reissue")
    public ApiResponse<?> reissueAccessToken(HttpServletRequest req, HttpServletResponse res) {
        String refreshToken = getRefreshTokenFromCookie(req);
        Tokens tokens = tokenService.reissueAccessToken(refreshToken);
        // 쿠키에 refresh token 저장
        addRefreshTokenCookie(tokens.refreshToken(), res);
        return ApiResponse.success(HttpStatus.OK, tokens.accessToken());
    }

    /**
     * 로그아웃
     *
     * @param userDetail
     * @param req
     * @param res
     * @return
     */
    @GetMapping("/api/logout")
    public ApiResponse<?> logout(@AuthenticationPrincipal CustomUserDetails userDetail, HttpServletRequest req, HttpServletResponse res) {
        tokenService.removeRefreshToken(userDetail.getMemberId());
        removeRefreshTokenFromCookie(req, res);
        SecurityContextHolder.clearContext();
        return ApiResponse.success(HttpStatus.OK);
    }

    // 쿠키에 refresh token 저장
    private void addRefreshTokenCookie(String refreshToken, HttpServletResponse res) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // 운영용
        cookie.setSecure(false); // 개발용
        cookie.setMaxAge((int) tokenService.getRefreshDuration());
        cookie.setPath("/");
         cookie.setAttribute("SameSite", "Lax");
        res.addCookie(cookie);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest req) {
        //        Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("refresh_token"));
        Cookie refreshCookie = null;
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            throw new JwtException("refresh token doesn't exist");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshCookie = cookie;
                break;
            }
        }
        if (refreshCookie == null) {
            throw new JwtException("refresh token doesn't exist");
        }
        return refreshCookie.getValue();
    }

    private void removeRefreshTokenFromCookie(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                cookie = new Cookie("refresh_token", null);
                cookie.setMaxAge(0);
                cookie.setPath("/"); // 설정 안해주면 삭제 안됨
                res.addCookie(cookie);
                break;
            }
        }
    }
}
