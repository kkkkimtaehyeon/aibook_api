package com.kth.aibook.service.member;

import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.dto.member.MemberDto;
import com.kth.aibook.dto.member.MemberSimpleDto;

public interface MemberService {
    Long createMember(MemberCreateRequestDto createRequest);
    MemberSimpleDto getMemberSimpleInfoById(Long memberId);
    MemberDto getMemberByEmail(String email);
    MemberDto getMemberByOauthMemberId(String provider, long oauthUserId);

}
