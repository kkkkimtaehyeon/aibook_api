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
    public VoiceDubbingResponseDto requestVoiceDubbing(Story story, Long voiceId) {
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException("등록된 목소리가 없습니다."));
        VoiceDubbingRequestDto request = new VoiceDubbingRequestDto(story, voice);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceDubbingRequestDto> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<VoiceDubbingResponseDto> response = restTemplate.exchange(FASTAPI_URL, HttpMethod.POST, httpEntity, VoiceDubbingResponseDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("더빙 생성 요청 중 오류가 발생했습니다.");
    }
}
