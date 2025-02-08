package com.kth.aibook.service.member;

import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.dto.member.MemberDto;

public interface MemberService {
    Long createMember(MemberCreateRequestDto createRequest);
    MemberDto getMemberByEmail(String email);
    MemberDto getMemberByOauthMemberId(String provider, long oauthUserId);

}
