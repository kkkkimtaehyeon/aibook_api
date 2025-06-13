package com.kth.aibook.service.authentication.impl;

import com.kth.aibook.common.exception.AuthenticationFailException;
import com.kth.aibook.common.provider.JwtProvider;
import com.kth.aibook.dto.auth.TokenRequestDto;
import com.kth.aibook.dto.auth.Tokens;
import com.kth.aibook.entity.member.MemberRole;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private final Long memberId = 1L;
    private final String accessToken = "test.access.token";
    private final String refreshToken = "test.refresh.token";
    private final String newAccessToken = "new.access.token";
    private final String newRefreshToken = "new.refresh.token";
    private final TokenRequestDto tokenRequestDto = new TokenRequestDto(memberId, MemberRole.MEMBER);
    private final long refreshDuration = 86400000L; // 24시간

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        // setUp에서는 공통으로 사용되는 스터빙만 설정
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueAccessToken_Success() {
        // Given
        when(jwtProvider.getRefreshDuration()).thenReturn(refreshDuration);
        doNothing().when(jwtProvider).validateToken(refreshToken);
        when(jwtProvider.extractTokenRequestFromToken(refreshToken)).thenReturn(tokenRequestDto);
        when(valueOperations.get("refresh:" + memberId)).thenReturn(refreshToken);
        when(jwtProvider.generateAccessToken(tokenRequestDto)).thenReturn(newAccessToken);
        when(jwtProvider.generateRefreshToken(tokenRequestDto)).thenReturn(newRefreshToken);

        // When
        Tokens tokens = tokenService.reissueAccessToken(refreshToken);

        // Then
        assertNotNull(tokens);
        assertEquals(newAccessToken, tokens.accessToken());
        assertEquals(newRefreshToken, tokens.refreshToken());
        verify(valueOperations).set(eq("refresh:" + memberId), eq(newRefreshToken), eq(refreshDuration), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 리프레시 토큰 검증 실패")
    void reissueAccessToken_FailValidation() {
        // Given
        doThrow(new JwtException("Invalid token")).when(jwtProvider).validateToken(refreshToken);

        // When & Then
        assertThrows(JwtException.class, () -> tokenService.reissueAccessToken(refreshToken));
        verify(valueOperations, never()).get(anyString());
        verify(jwtProvider, never()).generateAccessToken(any(TokenRequestDto.class));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - Redis에 토큰 없음")
    void reissueAccessToken_TokenNotFoundInRedis() {
        // Given
        doNothing().when(jwtProvider).validateToken(refreshToken);
        when(jwtProvider.extractTokenRequestFromToken(refreshToken)).thenReturn(tokenRequestDto);
        when(valueOperations.get("refresh:" + memberId)).thenReturn(null);

        // When & Then
        assertThrows(JwtException.class, () -> tokenService.reissueAccessToken(refreshToken));
        verify(jwtProvider, never()).generateAccessToken(any(TokenRequestDto.class));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - Redis 토큰과 불일치")
    void reissueAccessToken_TokenMismatch() {
        // Given
        doNothing().when(jwtProvider).validateToken(refreshToken);
        when(jwtProvider.extractTokenRequestFromToken(refreshToken)).thenReturn(tokenRequestDto);
        when(valueOperations.get("refresh:" + memberId)).thenReturn("different.refresh.token");

        // When & Then
        JwtException exception = assertThrows(JwtException.class, () -> tokenService.reissueAccessToken(refreshToken));
        assertEquals("access token reissue failed: refresh token doesnt match", exception.getMessage());
        verify(jwtProvider, never()).generateAccessToken(any(TokenRequestDto.class));
    }

    @Test
    @DisplayName("토큰 발급 성공")
    void issueTokens_Success() {
        // Given
        when(jwtProvider.getRefreshDuration()).thenReturn(refreshDuration);
        when(jwtProvider.generateAccessToken(tokenRequestDto)).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(tokenRequestDto)).thenReturn(refreshToken);

        // When
        Tokens tokens = tokenService.issueTokens(tokenRequestDto);

        // Then
        assertNotNull(tokens);
        assertEquals(accessToken, tokens.accessToken());
        assertEquals(refreshToken, tokens.refreshToken());
        verify(valueOperations).set(eq("refresh:" + memberId), eq(refreshToken), eq(refreshDuration), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("토큰 발급 실패 - Redis 예외")
    void issueTokens_RedisException() {
        // Given
        when(jwtProvider.getRefreshDuration()).thenReturn(refreshDuration);
        when(jwtProvider.generateAccessToken(tokenRequestDto)).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(tokenRequestDto)).thenReturn(refreshToken);
        doThrow(new RuntimeException("Redis connection error")).when(valueOperations).set(
                eq("refresh:" + memberId), eq(refreshToken), eq(refreshDuration), eq(TimeUnit.MILLISECONDS));

        // When & Then
        AuthenticationFailException exception = assertThrows(AuthenticationFailException.class, () -> tokenService.issueTokens(tokenRequestDto));
        assertTrue(exception.getMessage().contains("login failed: fail to save refresh token"));
    }

    @Test
    @DisplayName("getRefreshDuration 메서드 테스트")
    void getRefreshDuration_Success() {
        // Given
        when(jwtProvider.getRefreshDuration()).thenReturn(refreshDuration);

        // When
        long duration = tokenService.getRefreshDuration();

        // Then
        assertEquals(refreshDuration, duration);
        verify(jwtProvider).getRefreshDuration();
    }

    @Test
    @DisplayName("accessToken 발급 테스트")
    void issueAccessToken_Success() {
        // Given
        when(jwtProvider.generateAccessToken(tokenRequestDto)).thenReturn(accessToken);

        // When
        String result = tokenService.issueAccessToken(tokenRequestDto);

        // Then
        assertEquals(accessToken, result);
    }

    @Test
    @DisplayName("refreshToken 발급 테스트")
    void issueRefreshToken_Success() {
        // Given
        when(jwtProvider.generateRefreshToken(tokenRequestDto)).thenReturn(refreshToken);

        // When
        String result = tokenService.issueRefreshToken(tokenRequestDto);

        // Then
        assertEquals(refreshToken, result);
    }
}