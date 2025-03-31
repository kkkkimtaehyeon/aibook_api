package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryCompleteRequestDto;
import com.kth.aibook.dto.story.StoryPageCreateRequestDto;
import com.kth.aibook.dto.story.StoryPatchRequestDto;
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

@RequiredArgsConstructor
@Service
public class StoryServiceImpl implements StoryService {
    private final StoryRepository storyRepository;
    private final StoryPageRepository storyPageRepository;
    private final MemberRepository memberRepository;

    // 동화 기초이야기 저장
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

    // 동화 페이지 저장
    @Transactional
    @Override
    public Long createStoryPage(Long storyId, StoryPageCreateRequestDto createRequest) {
        Story story = findStory(storyId);
        StoryPage storyPage = createRequest.toEntity();
        story.addStoryPage(storyPage);
        StoryPage savedStoryPage = storyPageRepository.save(storyPage);

        return savedStoryPage.getId();
    }

    // 동화 완성
    @Transactional
    @Override
    public Long completeStory(Long storyId, StoryCompleteRequestDto completeRequest) {
        Story story = findStory(storyId);
        story.completeStory(completeRequest);

        return story.getId();
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
