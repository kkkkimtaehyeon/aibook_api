package com.kth.aibook.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSimpleDto {
    private Long id;
    private String name;

    public MemberSimpleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    // 프로필 사진?
}
