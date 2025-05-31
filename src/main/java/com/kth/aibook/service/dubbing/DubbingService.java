package com.kth.aibook.service.dubbing;

import com.amazonaws.util.Base64;
import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.dto.story.VoiceDubbingRequestDtoV1;
import com.kth.aibook.dto.story.VoiceDubbingRequestDtoV2;
import com.kth.aibook.service.cloud.CloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class DubbingService {
    private static final String FASTAPI_URL_V1 = "http://localhost:8000" + "/v1/api/voice-cloning";
    private static final String FASTAPI_URL_V2 = "http://localhost:8000" + "/v2/api/voice-cloning";
    private static final String FASTAPI_URL_V3 = "http://localhost:8000" + "/v3/api/voice-cloning";

    private final RestTemplate restTemplate;
    private final CloudStorageService cloudStorageService;

    @Transactional
    // fastApi로 tts 생성 요청
    public void requestVoiceDubbingV1(VoiceDubbingRequestDtoV1 voiceDubbingRequestDtoV1) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceDubbingRequestDtoV1> httpEntity = new HttpEntity<>(voiceDubbingRequestDtoV1, headers);
        ResponseEntity<?> response = restTemplate.exchange(FASTAPI_URL_V1, HttpMethod.POST, httpEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new StoryDubbingException("더빙 생성 요청 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    // fastApi로 tts 생성 요청
    public void requestVoiceDubbingV2(VoiceDubbingRequestDtoV2 voiceDubbingRequestDtoV2) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceDubbingRequestDtoV2> httpEntity = new HttpEntity<>(voiceDubbingRequestDtoV2, headers);
        ResponseEntity<?> response = restTemplate.exchange(FASTAPI_URL_V2, HttpMethod.POST, httpEntity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new StoryDubbingException("더빙 생성 요청 중 오류가 발생했습니다.");
        }
    }

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

    // base64로 인코딩된 음성 파일을 S3에 업로드하고 URL을 반환
    public String uploadDubbingAudio(String base64Bytes) {
        if (base64Bytes == null) {
            throw new StoryDubbingException("더빙 중 오류가 발생했습니다. (더빙 누락)");
        }
        byte[] audioBytes = Base64.decode(base64Bytes);
        return cloudStorageService.uploadBytes(audioBytes, "audio/wav");
    }


}
