package com.kth.aibook.service.authentication;

import com.kth.aibook.dto.auth.TokenRequestDto;
import com.kth.aibook.dto.auth.Tokens;

public interface TokenService {

    Tokens issueTokens(TokenRequestDto tokenRequestDto);

    String issueAccessToken(TokenRequestDto tokenRequestDto);

    String issueRefreshToken(TokenRequestDto tokenRequestDto);

    Tokens reissueAccessToken(String refreshToken);

    void removeRefreshToken(Long memberId);
    long getRefreshDuration();
}
