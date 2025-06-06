package com.kth.aibook.service.authentication.impl;

import com.kth.aibook.common.exception.JwtExpiredException;
import com.kth.aibook.common.provider.JwtProvider;
import com.kth.aibook.dto.auth.TokenRequestDto;
import com.kth.aibook.dto.auth.Tokens;
import com.kth.aibook.service.authentication.TokenService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;

    // access token, refresh token을 발급하고 redis에 저장
    @Override
    public Tokens issueTokens(TokenRequestDto tokenRequestDto) {
        String accessToken = issueAccessToken(tokenRequestDto);
        String refreshToken = issueRefreshToken(tokenRequestDto);
        saveRefreshTokenInRedis(tokenRequestDto.memberId(), refreshToken, jwtProvider.getRefreshDuration());
        return new Tokens(accessToken, refreshToken);
    }

    private void saveRefreshTokenInRedis(Long memberId, String refreshToken, long duration) {
        String key = getRefreshTokenKey(memberId);
        // redis에 refresh token을 저장
        try {
            redisTemplate.opsForValue().set(key, refreshToken, duration, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis exception while save refresh token: {}", e.getMessage());
            throw new JwtException("redis exception while save refresh token", e);
        }
    }

    @Override
    public String issueAccessToken(TokenRequestDto tokenRequestDto) {
        return jwtProvider.generateAccessToken(tokenRequestDto);
    }

    @Override
    public String issueRefreshToken(TokenRequestDto tokenRequestDto) {
        return jwtProvider.generateRefreshToken(tokenRequestDto);
    }
    // 한 트랜잭션으로 묶어줘야됨
    @Transactional
    @Override
    public Tokens reissueAccessToken(String refreshToken) {
        try {
            // rt 검증
            jwtProvider.validateToken(refreshToken);
            // redis에 저장된 rt와 비교
            TokenRequestDto tokenRequest = jwtProvider.extractTokenRequestFromToken(refreshToken);
            Long memberId = tokenRequest.memberId();
            String savedRefreshToken = getRefreshTokenFromRedis(memberId);
            if (!refreshToken.equals(savedRefreshToken)) {
                throw new JwtException("access token reissue failed: refresh token doesnt match");
            }
            // at, rt 재발급
            String newAccessToken = jwtProvider.generateAccessToken(tokenRequest);
            String newRefreshToken = jwtProvider.generateRefreshToken(tokenRequest);
            // redis에 rt를 새로 갱신
            setRefreshTokenInRedis(memberId, newRefreshToken);

            return new Tokens(newAccessToken, newRefreshToken);
        } catch (JwtExpiredException e) {
            throw new JwtExpiredException("re-login required", e);
        }
    }

    @Override
    public void removeRefreshToken(Long memberId) {
        String key = getRefreshTokenKey(memberId);
        redisTemplate.delete(key);
    }

    @Override
    public long getRefreshDuration() {
        return jwtProvider.getRefreshDuration();
    }

    private String getRefreshTokenKey(Long memberId) {
        return "refresh:" + memberId;
    }

    private String getRefreshTokenFromRedis(Long memberId) {
        String refreshTokenKey = getRefreshTokenKey(memberId);
        String refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);
        if (refreshToken == null) {
            throw new JwtException("refresh token(key=" + refreshTokenKey + ") not found in Redis");
        }
        return refreshToken;
    }

    private void setRefreshTokenInRedis(Long memberId, String refreshToken) {
        String refreshTokenKey = getRefreshTokenKey(memberId);
        redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, getRefreshDuration(), TimeUnit.MILLISECONDS);
    }
}
