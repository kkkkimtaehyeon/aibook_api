package com.kth.aibook.dto.auth;

import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.entity.member.MemberRole;

public record TokenRequestDto(
        Long memberId,
        MemberRole role
) {
    public TokenRequestDto(MemberDetailDto memberDetailDto) {
        this(memberDetailDto.getMemberId(), memberDetailDto.getRole());
    }
}
