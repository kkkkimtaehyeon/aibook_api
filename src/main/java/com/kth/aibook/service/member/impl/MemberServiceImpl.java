package com.kth.aibook.service.member.impl;

import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.dto.member.MemberDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.OauthMember;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.repository.member.OauthMemberRepository;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final OauthMemberRepository oauthMemberRepository;


    @Transactional
    @Override
    public Long createMember(MemberCreateRequestDto createRequest) {
        Long oauthProviderMemberProviderId = createRequest.getOauthProviderMemberId();
        String oauthProvider = createRequest.getOauthProvider();

        // TODO: oauth member 없으면 그냥 로그인으로 취급하게 바꿔야됨
        OauthMember oauthMember = oauthMemberRepository
                .findOauthMember(oauthProvider, oauthProviderMemberProviderId)
                .orElseGet(() -> saveOauthMember(oauthProvider, oauthProviderMemberProviderId));
        Member savedMember = memberRepository.save(createRequest.toEntity());
        savedMember.setOauthMember(oauthMember);

        return savedMember.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDto getMemberByEmail(String email) {
        Member member = memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberDto(member);
    }

    @Transactional
    @Override
    public MemberDto getMemberByOauthMemberId(String oauthProvider, long oauthProviderMemberId) {
        OauthMember oauthMember = oauthMemberRepository
                .findOauthMember(oauthProvider, oauthProviderMemberId)
                .orElseGet(() -> saveOauthMember(oauthProvider, oauthProviderMemberId));
        Member member = memberRepository
                .findByOauthMember(oauthMember)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberDto(member);
    }

    private OauthMember saveOauthMember(String oauthProvider, long oauthProviderMemberId) {
        return oauthMemberRepository.save(new OauthMember(oauthProvider, oauthProviderMemberId));
    }
}
