package com.kth.aibook.service.dubbing;

import com.amazonaws.util.Base64;
import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.common.exception.VoiceNotFoundException;
import com.kth.aibook.dto.story.VoiceDubbingRequestDto;
import com.kth.aibook.dto.story.VoiceDubbingResponseDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryDubbing;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.member.VoiceRepository;
import com.kth.aibook.repository.story.StoryDubbingRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.cloud.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class DubbingService {
    private static final String FASTAPI_URL = "http://localhost:8000" + "/api/voice-cloning";

    private final RestTemplate restTemplate;
    private final VoiceRepository voiceRepository;
    private final StoryRepository storyRepository;
    private final CloudStorageService cloudStorageService;

    @Transactional
    // fastApi로 tts 생성 요청
    public void requestVoiceDubbing(Long storyId, Long voiceId, Long memberId) {
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException("등록된 목소리가 없습니다."));
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new RuntimeException("존재하지 않는 동화입니다."));

        String webhookUrl = getWebhookUrl(story.getId(), voiceId, memberId);
        VoiceDubbingRequestDto request = new VoiceDubbingRequestDto(story, voice, webhookUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceDubbingRequestDto> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<?> response = restTemplate.exchange(FASTAPI_URL, HttpMethod.POST, httpEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new StoryDubbingException("더빙 생성 요청 중 오류가 발생했습니다.");
        }
    }

    // base64로 인코딩된 음성 파일을 S3에 업로드하고 URL을 반환
    public String uploadDubbingAudio(String base64Bytes) {
        if (base64Bytes == null) {
            throw new StoryDubbingException("더빙 중 오류가 발생했습니다. (더빙 누락)");
        }
        byte[] audioBytes = Base64.decode(base64Bytes);
        return cloudStorageService.uploadBytes(audioBytes, "audio/wav");
    }


    private String getWebhookUrl(Long storyId, Long voiceId, Long memberId) {
        return String.format("http://localhost:8080/api/stories/%d/members/%d/voices/%d/dubbing/completed", storyId, memberId, voiceId);
    }
}
