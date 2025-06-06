package com.kth.aibook.controller.auth;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.dto.auth.TokenRequestDto;
import com.kth.aibook.dto.auth.Tokens;
import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.dto.oauth.KakaoOauthProviderDto;
import com.kth.aibook.entity.member.MemberRole;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.authentication.TokenService;
import com.kth.aibook.service.authentication.impl.KakaoAuthenticationService;
import com.kth.aibook.service.member.MemberService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private KakaoAuthenticationService kakaoAuthenticationService;

    @Mock
    private MemberService memberService;

    @Mock
    private TokenService tokenService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthController authController;

    private final String code = "authorization_code";
    private final String kakaoToken = "kakao_access_token";
    private final Long oauthProviderMemberId = 12345L;
    private final Long memberId = 1L;
    private final String accessToken = "test.access.token";
    private final String refreshToken = "test.refresh.token";
    private final String newAccessToken = "new.access.token";
    private final String newRefreshToken = "new.refresh.token";
    private final MemberDetailDto memberDetailDto = new MemberDetailDto(memberId, "test@example.com", "Test User", LocalDate.now(), LocalDateTime.now(), MemberRole.MEMBER);
    private final TokenRequestDto tokenRequestDto = new TokenRequestDto(memberId, MemberRole.MEMBER);
    private final long refreshDuration = 86400000L; // 24시간

    private MockHttpServletResponse response;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    @DisplayName("카카오 로그인 - 이미 가입된 회원")
    void kakaoLogin_ExistingMember() {
        // Given
        when(kakaoAuthenticationService.getAccessToken(code)).thenReturn(kakaoToken);
        when(kakaoAuthenticationService.getUserIdFromToken(kakaoToken)).thenReturn(oauthProviderMemberId);
        when(memberService.getMemberByOauthMemberId("kakao", oauthProviderMemberId)).thenReturn(memberDetailDto);
        when(tokenService.issueTokens(any(TokenRequestDto.class))).thenReturn(new Tokens(accessToken, refreshToken));
        when(tokenService.getRefreshDuration()).thenReturn(refreshDuration);

        // When
        ResponseEntity<ApiResponseBody> response = authController.kakaoLogin(code, this.response);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accessToken, response.getBody().data());

        // 쿠키 검증
        Cookie refreshTokenCookie = this.response.getCookie("refresh-token");
        assertNotNull(refreshTokenCookie);
        assertEquals(refreshToken, refreshTokenCookie.getValue());
        assertTrue(refreshTokenCookie.isHttpOnly());
//        assertTrue(refreshTokenCookie.getSecure());
        assertEquals((int) refreshDuration, refreshTokenCookie.getMaxAge());
    }

    @Test
    @DisplayName("카카오 로그인 - 신규 회원")
    void kakaoLogin_NewMember() {
        // Given
        when(kakaoAuthenticationService.getAccessToken(code)).thenReturn(kakaoToken);
        when(kakaoAuthenticationService.getUserIdFromToken(kakaoToken)).thenReturn(oauthProviderMemberId);
        when(memberService.getMemberByOauthMemberId("kakao", oauthProviderMemberId))
                .thenThrow(new MemberNotFoundException("회원을 찾을 수 없습니다."));

        // When
        ResponseEntity<ApiResponseBody> response = authController.kakaoLogin(code, this.response);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("signup required", response.getBody().message());
        assertTrue(response.getBody().data() instanceof KakaoOauthProviderDto);
        KakaoOauthProviderDto dto = (KakaoOauthProviderDto) response.getBody().data();
        assertEquals("kakao", dto.oauthProvider());
        assertEquals(oauthProviderMemberId, dto.oauthProviderMemberId());

        // 쿠키가 설정되지 않았는지 확인
        assertNull(this.response.getCookie("refresh-token"));
    }

//    @Test
//    @DisplayName("액세스 토큰 재발급 테스트")
//    void reissueAccessToken() {
//        // Given
//        Cookie refreshTokenCookie = new Cookie("refresh-token", refreshToken);
//        request.setCookies(refreshTokenCookie);
//
//        when(tokenService.reissueAccessToken(refreshToken)).thenReturn(new Tokens(newAccessToken, newRefreshToken));
//        when(tokenService.getRefreshDuration()).thenReturn(refreshDuration);
//
//        // When
//        ResponseEntity<ApiResponseBody> apiResponse = authController.reissueAccessToken(request, response);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatus());
//        assertEquals(newAccessToken, apiResponse.getBody().data());
//
//        // 새로운 리프레시 토큰이 쿠키에 저장되었는지 확인
//        Cookie newRefreshTokenCookie = response.getCookie("refresh-token");
//        assertNotNull(newRefreshTokenCookie);
//        assertEquals(newRefreshToken, newRefreshTokenCookie.getValue());
//        assertTrue(newRefreshTokenCookie.isHttpOnly());
////        assertTrue(newRefreshTokenCookie.getSecure());
//        assertEquals((int) refreshDuration, newRefreshTokenCookie.getMaxAge());
//    }

    @Test
    @DisplayName("리프레시 토큰 없이 액세스 토큰 재발급 시도")
    void reissueAccessToken_WithoutRefreshToken() {
        // Given
        request.setCookies(new Cookie("other_cookie", "value"));

        // When & Then
        JwtException exception = assertThrows(JwtException.class, () -> authController.reissueAccessToken(request, response));
        assertEquals("refresh token doesn't exist", exception.getMessage());
    }

//    @Test
//    @DisplayName("로그아웃 테스트")
//    void logout() {
//        // When
//        ApiResponse<?> apiResponse = authController.logout();
//
//        // Then
//        assertEquals(HttpStatus.OK, apiResponse.getCode());
//        assertNull(apiResponse.getData());
//    }
}