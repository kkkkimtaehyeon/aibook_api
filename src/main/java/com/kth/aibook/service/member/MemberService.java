package com.kth.aibook.service.member;

import com.kth.aibook.dto.member.MemberDto;

public interface MemberService {
    MemberDto getMemberByEmail(String email);

}
