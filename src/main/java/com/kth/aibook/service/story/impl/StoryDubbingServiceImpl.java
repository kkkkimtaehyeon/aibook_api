package com.kth.aibook.service.story.impl;

import com.kth.aibook.common.exception.StoryDubbingException;
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
import java.util.List;
import java.util.Map;

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

    private static final String FAST_API_URL = "http://localhost:8000";

    // 더빙 추가
    @Transactional
    @Override
    public void addCustomDubbing(Long storyId, List<MultipartFile> files) {
        Story story = findStory(storyId);
        List<StoryPage> pages = story.getStoryPages();
        files.forEach(file -> {
            int pageNumber = extractPageNumber(file);
            String dubbingAudioUrl = cloudStorageService.uploadFile(file);
            pages.get(pageNumber).addDubbing(dubbingAudioUrl);
        });
    }

    @Override
    public void requestDubbing(Long storyId, Long voiceId, Long memberId) throws StoryDubbingException {
        dubbingService.requestVoiceDubbing(storyId, voiceId, memberId);
    }

    @Transactional
    @Override
    public void saveDubbing(Long storyId, Long memberId, Long voiceId, VoiceDubbingResponseDto response) throws StoryDubbingException {
        Story story = findStory(storyId);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다. memberId: " + memberId));
        Voice voice = voiceRepository.findById(voiceId).orElseThrow(() -> new RuntimeException("존재하지 않는 보이스입니다. voiceId: " + voiceId));

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
}
