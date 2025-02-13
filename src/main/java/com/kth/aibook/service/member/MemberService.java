package com.kth.aibook.service.member;

import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.dto.member.MemberSimpleDto;

public interface MemberService {
    Long createMember(MemberCreateRequestDto createRequest);

    MemberSimpleDto getMemberSimpleInfoById(Long memberId);

    MemberDetailDto getMemberByEmail(String email);

    MemberDetailDto getMemberByOauthMemberId(String provider, long oauthUserId);

    MemberDetailDto getMemberDetailInfoById(Long memberId);

}
