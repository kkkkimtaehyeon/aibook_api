package com.kth.aibook.service.member.impl;

import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.dto.member.MemberSimpleDto;
import com.kth.aibook.dto.member.VoiceUploadRequestDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.OauthMember;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.repository.member.OauthMemberRepository;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.member.VoiceRepository;
import com.kth.aibook.service.cloud.CloudStorageService;
import com.kth.aibook.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final VoiceRepository voiceRepository;
    private final CloudStorageService cloudStorageService;


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
    public MemberSimpleDto getMemberSimpleInfoById(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberSimpleDto(memberId, member.getNickName());
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDetailDto getMemberByEmail(String email) {
        Member member = memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberDetailDto(member);
    }

    @Transactional
    @Override
    public MemberDetailDto getMemberByOauthMemberId(String oauthProvider, long oauthProviderMemberId) {
        OauthMember oauthMember = oauthMemberRepository
                .findOauthMember(oauthProvider, oauthProviderMemberId)
                .orElseGet(() -> saveOauthMember(oauthProvider, oauthProviderMemberId));
        Member member = memberRepository
                .findByOauthMember(oauthMember)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberDetailDto(member);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDetailDto getMemberDetailInfoById(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        return new MemberDetailDto(member);
    }

    @Override
    public Long uploadVoice(Long memberId, VoiceUploadRequestDto voiceUploadRequest) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        String audioUrl = cloudStorageService.uploadFile(voiceUploadRequest.getAudioFile());
        Voice voice = Voice.builder()
                .name(voiceUploadRequest.getName())
                .audioUrl(audioUrl)
                .member(member)
                .build();
        Voice savedVoice = voiceRepository.save(voice);
        return savedVoice.getId();
    }

    private OauthMember saveOauthMember(String oauthProvider, long oauthProviderMemberId) {
        return oauthMemberRepository.save(new OauthMember(oauthProvider, oauthProviderMemberId));
    }
}
