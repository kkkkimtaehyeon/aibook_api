package com.kth.aibook.service.voice.impl;

import com.kth.aibook.common.exception.FileUploadException;
import com.kth.aibook.common.exception.VoiceNotFoundException;
import com.kth.aibook.dto.member.VoiceDto;
import com.kth.aibook.dto.member.VoiceUploadRequestDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.member.VoiceRepository;
import com.kth.aibook.service.cloud.CloudStorageService;
import com.kth.aibook.service.voice.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoiceServiceImpl implements VoiceService {
    private final MemberRepository memberRepository;
    private final VoiceRepository voiceRepository;
    private final CloudStorageService cloudStorageService;

    @Transactional
    @Override
    public long uploadMyVoice(Long memberId, VoiceUploadRequestDto voiceUploadRequest) {
        Member member = findMember(memberId);
        String uploadedFileUrl = cloudStorageService.uploadMultiPartFile(voiceUploadRequest.getAudioFile());
        try {
            Voice voice = Voice.builder()
                    .name(voiceUploadRequest.getName())
                    .audioUrl(uploadedFileUrl)
                    .member(member)
                    .isDefault(false)
                    .build();
            Voice savedVoice = voiceRepository.save(voice);
            return savedVoice.getId();
        } catch (RuntimeException e) {
            // 보상 트랜잭션: s3 파일 삭제
            cloudStorageService.removeFile(uploadedFileUrl);
            throw new FileUploadException("목소리를 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void deleteVoice(Long memberId, Long voiceId) {
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new VoiceNotFoundException(voiceId));
        // s3 오디오는 삭제?
        voice.logicallyDeleteVoice();
    }

    @Override
    public List<VoiceDto> getVoices(Long memberId) {
        return voiceRepository.findByMemberId(memberId);
    }

    private Member findMember(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
    }
}
