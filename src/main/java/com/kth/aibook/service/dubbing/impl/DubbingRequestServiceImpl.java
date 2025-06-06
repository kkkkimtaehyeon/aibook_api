package com.kth.aibook.service.dubbing.impl;

import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.dto.story.DubbingContentAndPreSignedUrlDto;
import com.kth.aibook.dto.story.VoiceDubbingRequestDtoV2;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.service.cloud.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DubbingRequestServiceImpl {
    private static final String FASTAPI_URL_V3 = "http://localhost:8000" + "/v3/api/voice-cloning";
    private final CloudStorageService cloudStorageService;
    private final RestTemplate restTemplate;

    @Transactional
    // fastApi로 tts 생성 요청
    public void requestVoiceDubbingV3(VoiceDubbingRequestDtoV2 voiceDubbingRequestDtoV2) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceDubbingRequestDtoV2> httpEntity = new HttpEntity<>(voiceDubbingRequestDtoV2, headers);
        ResponseEntity<?> response = restTemplate.exchange(FASTAPI_URL_V3, HttpMethod.POST, httpEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new StoryDubbingException("더빙 생성 요청 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public Map<Long, DubbingContentAndPreSignedUrlDto> requestDubbing(Story story, Voice voice, Long memberId, String requestId) {
        Long storyId = story.getId();
        Long voiceId = voice.getId();
        // 더빙 완료시 저장을 위한 웹훅 url
        String webHookUrl = getWebhookUrl(storyId, voiceId, memberId, requestId);

        Map<Long, DubbingContentAndPreSignedUrlDto> dubbingRequestMap = new HashMap<>();
        for(StoryPage page: story.getStoryPages()) {
            Long pageId = page.getId();
            // s3 업로드 키
            String key = page + "_" + voiceId + "_" + UUID.randomUUID() + ".wav";
            // ai 서버에서 오디오 업로드 할 url
            String preSignedUrl = cloudStorageService.getPreSignedUrlForUpdate(key);
            dubbingRequestMap.put(pageId, new DubbingContentAndPreSignedUrlDto(page.getContent(), preSignedUrl));
        }
        requestVoiceDubbingV3(new VoiceDubbingRequestDtoV2(voice.getAudioUrl(), dubbingRequestMap, webHookUrl));
        return dubbingRequestMap;
    }

    private String getWebhookUrl(Long storyId, Long voiceId, Long memberId, String requestId) {
        return String.format("http://localhost:8080/api/stories/%d/members/%d/voices/%d/dubbing/completed?request-id=%s", storyId, memberId, voiceId ,requestId);
    }
}
