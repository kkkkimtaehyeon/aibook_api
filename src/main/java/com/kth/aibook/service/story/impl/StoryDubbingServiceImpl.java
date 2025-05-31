package com.kth.aibook.service.story.impl;

import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.common.exception.VoiceNotFoundException;
import com.kth.aibook.dto.story.DubbingContentAndPreSignedUrlDto;
import com.kth.aibook.dto.story.VoiceDubbingRequestDtoV1;
import com.kth.aibook.dto.story.VoiceDubbingRequestDtoV2;
import com.kth.aibook.dto.story.VoiceDubbingResponseDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryDubbing;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.entity.story.StoryPageDubbing;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.member.VoiceRepository;
import com.kth.aibook.repository.story.StoryDubbingRepository;
import com.kth.aibook.repository.story.StoryPageDubbingRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.cloud.CloudStorageService;
import com.kth.aibook.service.dubbing.DubbingService;
import com.kth.aibook.service.story.StoryDubbingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StoryDubbingServiceImpl implements StoryDubbingService {
    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;
    private final VoiceRepository voiceRepository;
    private final StoryDubbingRepository storyDubbingRepository;
    private final StoryPageDubbingRepository storyPageDubbingRepository;

    private final DubbingService dubbingService;
    private final CloudStorageService cloudStorageService;

    private Map<String, Map<Long, String>> dubbingSavingMap = new HashMap<>();


    // 더빙 추가
//    @Transactional
//    @Override
//    public void addCustomDubbing(Long storyId, List<MultipartFile> files) {
//        Story story = findStory(storyId);
//        List<StoryPage> pages = story.getStoryPages();
//        files.forEach(file -> {
//            int pageNumber = extractPageNumber(file);
//            String dubbingAudioUrl = cloudStorageService.uploadFile(file);
//            pages.get(pageNumber).addDubbing(dubbingAudioUrl);
//        });
//    }

    @Transactional
    @Override
    public void requestDubbingV1(Long storyId, Long voiceId, Long memberId) throws StoryDubbingException {
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException("등록된 목소리가 없습니다."));
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다."));
        // 완료처리요청을 위한 web hook
        String requestId = String.valueOf(UUID.randomUUID()); // 요청을 식별하기 위한 uuid
        String webHookUrl = generateWebhookUrl(storyId, voiceId, memberId, requestId);
        // ai 서버에 더빙 요청
        dubbingService.requestVoiceDubbingV1(new VoiceDubbingRequestDtoV1(story, voice, webHookUrl));
    }

    @Transactional
    @Override
    public void requestDubbingV2(Long storyId, Long voiceId, Long memberId) throws StoryDubbingException {
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException("등록된 목소리가 없습니다."));
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다."));
        // 완료처리요청을 위한 web hook
        String requestId = String.valueOf(UUID.randomUUID()); // 요청을 식별하기 위한 uuid
        String webHookUrl = generateWebhookUrl(storyId, voiceId, memberId, requestId);
        // preSignedUrl 생성
        Map<Long, DubbingContentAndPreSignedUrlDto> dubbingRequestMap = new HashMap<>();
        Map<Long, String> dubbingAudioUrlMap = new HashMap<>();
        for (StoryPage page : story.getStoryPages()) {
            String key = page.getId() + "_" + voiceId + "_" + requestId + ".wav";
            String preSignedUrl = cloudStorageService.getPreSignedUrlForUpdate(key); // 파라미터 이전 url로 오디오 다운로드 가능
            dubbingRequestMap.put(page.getId(), new DubbingContentAndPreSignedUrlDto(page.getContent(), preSignedUrl));
            dubbingAudioUrlMap.put(page.getId(), extractAudioUrl(preSignedUrl));
        }
        dubbingSavingMap.put(requestId, dubbingAudioUrlMap);
        // ai 서버에 더빙 요청
        dubbingService.requestVoiceDubbingV2(new VoiceDubbingRequestDtoV2(voice.getAudioUrl(), dubbingRequestMap, webHookUrl));
    }

    @Transactional
    @Override
    public void requestDubbingV3(Long storyId, Long voiceId, Long memberId) throws StoryDubbingException {
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException("등록된 목소리가 없습니다."));
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다."));
        // 완료처리요청을 위한 web hook
        String requestId = String.valueOf(UUID.randomUUID()); // 요청을 식별하기 위한 uuid
        String webHookUrl = generateWebhookUrl(storyId, voiceId, memberId, requestId);
        // preSignedUrl 생성
        Map<Long, DubbingContentAndPreSignedUrlDto> dubbingRequestMap = new HashMap<>();
        Map<Long, String> dubbingAudioUrlMap = new HashMap<>();
        for (StoryPage page : story.getStoryPages()) {
            String key = page.getId() + "_" + voiceId + "_" + requestId + ".wav";
            String preSignedUrl = cloudStorageService.getPreSignedUrlForUpdate(key); // 파라미터 이전 url로 오디오 다운로드 가능
            dubbingRequestMap.put(page.getId(), new DubbingContentAndPreSignedUrlDto(page.getContent(), preSignedUrl));
            dubbingAudioUrlMap.put(page.getId(), extractAudioUrl(preSignedUrl));
        }
        dubbingSavingMap.put(requestId, dubbingAudioUrlMap);
        // ai 서버에 더빙 요청
        dubbingService.requestVoiceDubbingV3(new VoiceDubbingRequestDtoV2(voice.getAudioUrl(), dubbingRequestMap, webHookUrl));
    }

    @Transactional
    @Override
    public void saveDubbingV1(Long storyId, Long memberId, Long voiceId, VoiceDubbingResponseDto response) throws StoryDubbingException {
        Story story = findStory(storyId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다. memberId: " + memberId));
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new VoiceNotFoundException("존재하지 않는 보이스입니다. voiceId: " + voiceId));

        // 동화 더빙 저장
        StoryDubbing storyDubbing = StoryDubbing.builder()
                .member(member)
                .story(story)
                .voice(voice)
                .dubbedAt(LocalDateTime.now())
                .build();
        storyDubbingRepository.save(storyDubbing);
        Map<Long, String> storyDubbingMap = response.getStoryDubbingMap();
        // 오디오를 s3에 업로드하고 db 업데이트
        String base64Bytes;
        String dubbingAudioUrl;
        for (StoryPage storyPage : story.getStoryPages()) {
            base64Bytes = storyDubbingMap.get(storyPage.getId());
            dubbingAudioUrl = dubbingService.uploadDubbingAudio(base64Bytes);
            // 동화 더빙 페이지 저장
            StoryPageDubbing storyPageDubbing = new StoryPageDubbing(storyPage, dubbingAudioUrl);
            storyDubbing.addStoryDubbingPage(storyPageDubbing);
            storyPageDubbingRepository.save(storyPageDubbing);
        }
    }

    @Transactional
    @Override
    public void saveDubbingV2(Long storyId, Long memberId, Long voiceId, String requestId) throws StoryDubbingException {
        Story story = findStory(storyId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다. memberId: " + memberId));
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new VoiceNotFoundException("존재하지 않는 보이스입니다. voiceId: " + voiceId));
        // DB에 더빙 저장
        StoryDubbing storyDubbing = StoryDubbing.builder()
                .member(member)
                .story(story)
                .voice(voice)
                .dubbedAt(LocalDateTime.now())
                .build();
        // DB에 더빙 페이지 저장
        Map<Long, String> audioUrlMap = dubbingSavingMap.get(requestId);
        for (StoryPage page : story.getStoryPages()) {
            String audioUrl = audioUrlMap.get(page.getId());
            StoryPageDubbing storyPageDubbing = new StoryPageDubbing(page, audioUrl);
            storyDubbing.addStoryDubbingPage(storyPageDubbing);
        }
        storyDubbingRepository.save(storyDubbing);
    }

    private Story findStory(Long storyId) {
        return storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다. storyId: " + storyId));
    }

    private int extractPageNumber(MultipartFile file) {
        String ogFileName = file.getOriginalFilename();
        if (ogFileName == null) {
            throw new RuntimeException("파일 이름을 찾을 수 없습니다.");
        }
        String pageNumber = ogFileName.split("-")[3].split("\\.")[0];
        return Integer.parseInt(pageNumber) - 1;
    }

    private String generateWebhookUrl(Long storyId, Long voiceId, Long memberId, String uuid) {
        return String.format("http://localhost:8080/api/stories/%d/members/%d/voices/%d/dubbing/completed?request-id=%s", storyId, memberId, voiceId, uuid);
    }

    private String extractAudioUrl(String preSignedUrl) {
        return preSignedUrl.split("\\?")[0]; // preSignedUrl에서 파라미터 제거한 url
    }
}