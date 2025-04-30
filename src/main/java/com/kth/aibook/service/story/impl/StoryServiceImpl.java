package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.*;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryPage;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.repository.member.MemberRepository;
import com.kth.aibook.repository.story.StoryPageRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long createStory(Long memberId, StoryCreateRequestDto request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다. memberId: " + memberId));
        Story story = Story.builder()
                .baseStory(request.baseStory())
                .createdAt(LocalDateTime.now())
                .member(member)
                .title(request.title())
                .isPublic(false)
                .isDubbed(false)
                .build();
        String[] sentences = request.selectedSentences();
        for (int pageNumber = 0; pageNumber < sentences.length; pageNumber++) {
            StoryPage page = StoryPage.builder()
                    .pageNumber(pageNumber)
                    .content(sentences[pageNumber])
                    .build();
            story.addStoryPage(page);
        }
        Story savedStory = storyRepository.save(story);
        return savedStory.getId();
    }

    // 동화 삭제
    @Override
    public void removeStory(Long storyId) {
        boolean isExists = storyRepository.existsById(storyId);
        if (!isExists) {
            throw new StoryNotFoundException("존재하지 않는 동화입니다.");
        }
        storyRepository.deleteById(storyId);
    }

    // 동화 수정
    @Transactional
    @Override
    public void patchStory(Long storyId, StoryPatchRequestDto patchRequest) {
        Story story = findStory(storyId);
        if (patchRequest != null && patchRequest.getTitle() != null) {
            story.updateTitle(patchRequest.getTitle());
        }
    }



    private Story findStory(Long storyId) {
        return storyRepository.findById(storyId).orElseThrow(() -> new StoryNotFoundException("존재하지 않는 동화입니다. storyId: " + storyId));
    }
}
