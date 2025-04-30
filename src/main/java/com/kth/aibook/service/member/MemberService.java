package com.kth.aibook.service.member;

import com.kth.aibook.dto.member.*;

import java.util.List;

public interface MemberService {
    Long createMember(MemberCreateRequestDto createRequest);

    MemberSimpleDto getMemberSimpleInfoById(Long memberId);

    MemberDetailDto getMemberByEmail(String email);

    MemberDetailDto getMemberByOauthMemberId(String provider, long oauthUserId);

    MemberDetailDto getMemberDetailInfoById(Long memberId);

    Long uploadVoice(Long memberId, VoiceUploadRequestDto voiceUploadRequest);

    List<VoiceDto> getVoices(Long memberId);

    void removeVoices(Long voiceId);

}
