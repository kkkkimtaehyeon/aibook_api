package com.kth.aibook.service.dubbing.impl;

import com.kth.aibook.common.exception.VoiceNotFoundException;
import com.kth.aibook.dto.dubbedStory.DubbedStoryCreateRequestDto;
import com.kth.aibook.dto.dubbedStory.DubbedStorySaveRequestDto;
import com.kth.aibook.dto.story.DubbingContentAndPreSignedUrlDto;
import com.kth.aibook.dto.story.StoryDubbingResponseDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryDubbing;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.entity.story.StoryPageDubbing;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.exception.storyDubbing.StoryDubbingNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.member.VoiceRepository;
import com.kth.aibook.repository.story.StoryDubbingRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.dubbing.StoryDubbingService2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StoryDubbingService2Impl implements StoryDubbingService2 {
    private final StoryDubbingRepository storyDubbingRepository;
    private final VoiceRepository voiceRepository;
    private final StoryRepository storyRepository;
    private final DubbingRequestServiceImpl dubbingRequestService;
    private final MemberRepository memberRepository;

    // dubbed-story를 저장하기 위해 사용하는 세션
    private final Map<String, Map<Long, DubbingContentAndPreSignedUrlDto>> dubbedStorySession = new HashMap<>();

    // 더빙 요청
    @Transactional
    @Override
    public void requestDubbedStory(Long memberId, DubbedStoryCreateRequestDto request) {
        String requestId = UUID.randomUUID().toString();
        Story story = storyRepository.findById(request.storyId())
                .orElseThrow(() -> new StoryNotFoundException(request.storyId()));
        Voice voice = voiceRepository.findById(request.voiceId())
                .orElseThrow(() -> new VoiceNotFoundException(request.voiceId()));
        // Ai 서버에 더빙 오디오 생성 요청
        Map<Long, DubbingContentAndPreSignedUrlDto> dubbingMap = dubbingRequestService.requestDubbing(story, voice, memberId, requestId);
        // 세션에 저장
        dubbedStorySession.put(requestId, dubbingMap);
    }

    // 더빙 저장
    @Transactional
    @Override
    public Long saveDubbedStory(Long storyId, Long memberId, Long voiceId, String requestId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new StoryNotFoundException(storyId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다. memberId: " + memberId));
        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new VoiceNotFoundException(voiceId));
        // 세션에서 임시 저장한 dubbing map을 조회
        Map<Long, DubbingContentAndPreSignedUrlDto> dubbingMap = dubbedStorySession.get(requestId);
        // DB에 더빙 저장
        StoryDubbing storyDubbing = StoryDubbing.builder()
                .member(member)
                .story(story)
                .voice(voice)
                .dubbedAt(LocalDateTime.now())
                .build();
        // 더빙 페이지 추가
        for(StoryPage page: story.getStoryPages()) {
            DubbingContentAndPreSignedUrlDto contentAndUrl = dubbingMap.get(page.getId());
            String audioUrl = contentAndUrl.preSignedUrl().split("\\?")[0];
            storyDubbing.addStoryDubbingPage(new StoryPageDubbing(page, audioUrl));
        }
        StoryDubbing savedStoryDubbing = storyDubbingRepository.save(storyDubbing);
        return savedStoryDubbing.getId();
    }

    // 목록조회
    @Override
    public List<StoryDubbingResponseDto> getDubbedStories(Long memberId, Pageable pageable) {
        List<StoryDubbing> storyDubbings = storyDubbingRepository.findByMemberId(memberId, pageable);
        return storyDubbings.stream().map(StoryDubbingResponseDto::new).toList();
    }

    // 상세조회
    @Transactional(readOnly = true)
    @Override
    public StoryDubbingResponseDto getDubbedStory(Long dubbedStoryId) {
        StoryDubbing storyDubbing = storyDubbingRepository.findById(dubbedStoryId)
                .orElseThrow(() -> new StoryDubbingNotFoundException(dubbedStoryId));
        return new StoryDubbingResponseDto(storyDubbing);
    }

    // 삭제
    @Override
    public void removeStoryDubbing(Long storyDubbingId) {
        storyDubbingRepository.deleteById(storyDubbingId);
    }
}
