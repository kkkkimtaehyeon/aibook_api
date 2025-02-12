package com.kth.aibook.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSimpleDto {
    private Long memberId;
    private String memberName;

    public MemberSimpleDto(Long memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
    // 프로필 사진?
}
