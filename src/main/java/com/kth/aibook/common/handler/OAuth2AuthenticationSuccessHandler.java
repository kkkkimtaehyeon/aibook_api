package com.kth.aibook.common.handler;

import com.kth.aibook.common.provider.JwtProvider;
import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        try {
            MemberDetailDto memberDetailDto = memberService.getMemberByEmail(email);

        } catch (MemberNotFoundException e) {
            // 회원이 없으면 회원가입
            response.sendRedirect("/members/register?email=" + email);
        }
        response.sendRedirect("/");

    }
}
