package com.kth.aibook.service.story.impl;

import com.amazonaws.util.Base64;
import com.kth.aibook.dto.story.*;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.story.StoryPageRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.cloud.CloudStorageService;
import com.kth.aibook.service.dubbing.DubbingService;
import com.kth.aibook.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final StoryPageRepository storyPageRepository;
    private final MemberRepository memberRepository;

    private final DubbingService dubbingService;
    private final CloudStorageService cloudStorageService;

    @Override
    public Long createBaseStory(long memberId, BaseStoryCreateRequestDto createRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Story tempStory = Story.builder()
                .baseStory(createRequest.baseStory())
                .member(member)
                .build();
        Story savedStory = storyRepository.save(tempStory);

        return savedStory.getId();
    }

    @Transactional
    @Override
    public Long createStoryPage(Long storyId, StoryPageCreateRequestDto createRequest) {
        Story story = findStory(storyId);
        StoryPage storyPage = createRequest.toEntity();
        story.addStoryPage(storyPage);
        StoryPage savedStoryPage = storyPageRepository.save(storyPage);

        return savedStoryPage.getId();
    }

    @Transactional
    @Override
    public Long completeStory(Long storyId, StoryCompleteRequestDto completeRequest) {
        Story story = findStory(storyId);
        story.completeStory(completeRequest);

        return story.getId();
    }

    @Override
    public void removeStory(Long storyId) {
        boolean isExists = storyRepository.existsById(storyId);
        if (!isExists) {
            throw new StoryNotFoundException("존재하지 않는 동화입니다.");
        }
        storyRepository.deleteById(storyId);
    }

    // 더빙 추가
    @Transactional
    @Override
    public void addDubbings(Long storyId, List<MultipartFile> files) {
        Story story = findStory(storyId);
        List<StoryPage> pages = story.getStoryPages();
        files.forEach(file -> {
            int pageNumber = extractPageNumber(file);
            String dubbingAudioUrl = cloudStorageService.uploadFile(file);
            pages.get(pageNumber).addDubbing(dubbingAudioUrl);
        });
    }

    @Transactional
    @Override
    public void requestDubbing(Long storyId, Long voiceId, Long memberId) {
        Story story = findStory(storyId);
        dubbingService.requestVoiceDubbing(story, voiceId, memberId);
//        Map<Long, String> storyDubbingMap = response.getStoryDubbingMap();
//        // 오디오를 s3에 업로드하고 db 업데이트
//        String base64Bytes;
//        byte[] audioBytes;
//        String dubbingAudioUrl;
//        for (StoryPage storyPage: story.getStoryPages()) {
//            base64Bytes = storyDubbingMap.get(storyPage.getId());
//            if (base64Bytes == null) {
//                throw new RuntimeException("보이스 더빙 중 오류가 발생했습니다. (더빙 누락)");
//            }
//            audioBytes = Base64.decode(base64Bytes);
//            dubbingAudioUrl = cloudStorageService.uploadBytes(audioBytes, "audio/wav");
//            storyPage.addDubbing(dubbingAudioUrl);
//        }
    }

    @Transactional // 없으면 lazy loading 에러 발생
    @Override
    public void saveDubbing(Long storyId, VoiceDubbingResponseDto response) {
        Story story = findStory(storyId);
        Map<Long , String> storyDubbingMap = response.getStoryDubbingMap();
        // 오디오를 s3에 업로드하고 db 업데이트
        String base64Bytes;
        byte[] audioBytes;
        String dubbingAudioUrl;
        for (StoryPage storyPage: story.getStoryPages()) {
            base64Bytes = storyDubbingMap.get(storyPage.getId());
            if (base64Bytes == null) {
                throw new RuntimeException("보이스 더빙 중 오류가 발생했습니다. (더빙 누락)");
            }
            audioBytes = Base64.decode(base64Bytes);
            dubbingAudioUrl = cloudStorageService.uploadBytes(audioBytes, "audio/wav");
            storyPage.addDubbing(dubbingAudioUrl);
        }
    }

    @Transactional
    @Override
    public void patchStory(Long storyId, StoryPatchRequestDto patchRequest) {
        Story story = findStory(storyId);
        if (patchRequest != null && patchRequest.getTitle() != null) {
            story.updateTitle(patchRequest.getTitle());
        }
    }


    private Story findStory(Long storyId) {
        return storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다."));
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
