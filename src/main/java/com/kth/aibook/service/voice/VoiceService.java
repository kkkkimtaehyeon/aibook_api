package com.kth.aibook.service.voice;

import com.kth.aibook.dto.member.VoiceDto;
import com.kth.aibook.dto.member.VoiceUploadRequestDto;

import java.util.List;

public interface VoiceService {
    long uploadMyVoice(Long memberId, VoiceUploadRequestDto voiceUploadRequest);
    void deleteVoice(Long memberId, Long voiceId);
    List<VoiceDto> getVoices(Long memberId);
}
