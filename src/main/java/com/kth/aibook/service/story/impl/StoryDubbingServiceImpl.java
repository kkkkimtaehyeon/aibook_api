package com.kth.aibook.service.story.impl;

import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.common.exception.VoiceNotFoundException;
import com.kth.aibook.dto.story.DubbingContentAndPreSignedUrlDto;
import com.kth.aibook.dto.story.VoiceDubbingRequestDtoV2;
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
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.cloud.CloudStorageService;
import com.kth.aibook.service.dubbing.impl.DubbingRequestServiceImpl;
import com.kth.aibook.service.story.StoryDubbingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final DubbingRequestServiceImpl dubbingRequestServiceImpl;
    private final CloudStorageService cloudStorageService;

    private Map<String, Map<Long, String>> dubbingSavingMap = new HashMap<>();

    @Transactional
    @Override
    public void requestDubbing(Long storyId, Long voiceId, Long memberId) throws StoryDubbingException {
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new VoiceNotFoundException(voiceId));
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException(storyId));
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
        dubbingRequestServiceImpl.requestVoiceDubbingV3(new VoiceDubbingRequestDtoV2(voice.getAudioUrl(), dubbingRequestMap, webHookUrl));
    }

    @Transactional
    @Override
    public void saveDubbing(Long storyId, Long memberId, Long voiceId, String requestId) throws StoryDubbingException {
        Story story = findStory(storyId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다. memberId: " + memberId));
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new VoiceNotFoundException(voiceId));
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

    @Override
    public void deleteStoryDubbing(Long storyDubbingId) {
        boolean isExist = storyDubbingRepository.existsById(storyDubbingId);
        if (isExist) {
            storyDubbingRepository.deleteById(storyDubbingId);
        }
    }

    private Story findStory(Long storyId) {
        return storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException(storyId));
    }
    private String generateWebhookUrl(Long storyId, Long voiceId, Long memberId, String uuid) {
        return String.format("http://localhost:8080/api/stories/%d/members/%d/voices/%d/dubbing/completed?request-id=%s", storyId, memberId, voiceId, uuid);
    }

    private String extractAudioUrl(String preSignedUrl) {
        return preSignedUrl.split("\\?")[0]; // preSignedUrl에서 파라미터 제거한 url
    }
}