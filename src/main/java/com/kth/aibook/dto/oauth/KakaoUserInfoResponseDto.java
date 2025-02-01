package com.kth.aibook.dto.oauth;

public record KakaoUserInfoResponseDto(
        long id,
        String connected_at,
        Properties properties,
        KakaoAccount kakao_account
) {
    public record Properties(String nickname) {}

    public record KakaoAccount(
            boolean profile_nickname_needs_agreement,
            Profile profile,
            boolean has_email,
            boolean email_needs_agreement,
            boolean is_email_valid,
            boolean is_email_verified,
            String email
    ) {
        public record Profile(String nickname, boolean is_default_nickname) {}
    }
}


// 응답
//{"id":3803153223,
//        "connected_at":"2024-11-22T08:25:31Z",
//        "properties":{"nickname":"김태현"},"kakao_account":{"profile_nickname_needs_agreement":false,"profile":{"nickname":"김태현","is_default_nickname":false},"has_email":true,"email_needs_agreement":false,"is_email_valid":true,"is_email_verified":true,"email":"kth1927@naver.com"}}
