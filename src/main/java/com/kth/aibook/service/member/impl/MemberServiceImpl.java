package com.kth.aibook.service.member.impl;

import com.kth.aibook.dto.member.MemberDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberDto(member);
    }
}
