package com.kth.aibook.common.provider;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.common.exception.JwtExpiredException;
import com.kth.aibook.dto.auth.TokenRequestDto;
import com.kth.aibook.entity.member.MemberRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    private Key key;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-duration}")
    private long accessDuration;

    @Getter
    @Value("${jwt.refresh-duration}")
    private long refreshDuration;

    @PostConstruct
    public void init() {
        byte[] byteSecretKey = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public String generateAccessToken(TokenRequestDto tokenRequestDto) {
        return generateJwt(tokenRequestDto, accessDuration);
    }

    public String generateRefreshToken(TokenRequestDto tokenRequestDto) {
        return generateJwt(tokenRequestDto, refreshDuration);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("token expired", e);
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰이 변조되었습니다.", e);
        } catch (Exception e) {
            throw new JwtException("토큰을 파싱하는 중 오류가 발생했습니다", e);
        }
    }

    public Authentication getAuthentication(String token) {
        validateToken(token);

        Long memberId = getMemberId(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(token);

        CustomUserDetails userDetails = new CustomUserDetails(memberId, authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    public TokenRequestDto extractTokenRequestFromToken(String token) {
        Claims claims = parseToken(token);
        Long memberId = Long.valueOf(claims.getSubject());
        MemberRole role = MemberRole.valueOf((String) claims.get("role"));
        return new TokenRequestDto(memberId, role);
    }

    private Long getMemberId(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    private List<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = parseToken(token);
        String role = claims.get("role").toString();
        return List.of(new SimpleGrantedAuthority(role));
    }

    private String generateJwt(TokenRequestDto tokenRequestDto, long duration) {
        long currentMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(String.valueOf(tokenRequestDto.memberId()))
                .claim("role", tokenRequestDto.role())
                .setIssuedAt(new Date(currentMillis))
                .setExpiration(new Date(currentMillis + duration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
