package com.kth.aibook.service.dubbing;

import com.kth.aibook.common.exception.VoiceNotFoundException;
import com.kth.aibook.dto.story.VoiceDubbingRequestDto;
import com.kth.aibook.dto.story.VoiceDubbingResponseDto;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.repository.member.VoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class DubbingService {
    private final RestTemplate restTemplate;
    private final VoiceRepository voiceRepository;
    private static final String FASTAPI_URL = "http://localhost:8000" + "/api/voice-cloning";

    @Transactional
    // fastApi로 tts 생성 요청
    public void requestVoiceDubbing(Story story, Long voiceId, Long memberId) {
        //

        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException("등록된 목소리가 없습니다."));
        String webhookUrl = getWebhookUrl(story.getId(), voiceId, memberId);
        VoiceDubbingRequestDto request = new VoiceDubbingRequestDto(story, voice, webhookUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceDubbingRequestDto> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<?> response = restTemplate.exchange(FASTAPI_URL, HttpMethod.POST, httpEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
        throw new RuntimeException("더빙 생성 요청 중 오류가 발생했습니다.");
        }
    }

    private String getWebhookUrl(Long storyId, Long voiceId, Long memberId) {
        return String.format("http://localhost:8080/api/stories/%d/voices/%d/dubbing/completed", storyId, voiceId);
    }
}
