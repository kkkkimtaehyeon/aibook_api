package com.kth.aibook.service.story.impl;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.dto.story.StorySearchRequestDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.repository.story.StoryLikeRepository;
import com.kth.aibook.repository.story.StoryQueryRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.story.StoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class StoryQueryServiceImpl implements StoryQueryService {
    private final StoryQueryRepository storyQueryRepository;
    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;

    private static final int MOST_VIEWED_STORY_SIZE = 4;
    private static final int MOST_LIKED_STORY_SIZE = 4;

    @Transactional(readOnly = true)
    @Override
    public List<StorySimpleResponseDto> getMostViewedStories() {
        return storyQueryRepository.findMostViewedStories(MOST_VIEWED_STORY_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StorySimpleResponseDto> getMostLikedStories() {
        return storyQueryRepository.findMostLikedStories(MOST_LIKED_STORY_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StorySimpleResponseDto> getPublicStories(Pageable pageable, StorySearchRequestDto searchRequest) {
        return storyQueryRepository.findStoryPages(pageable, searchRequest, true, null);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StorySimpleResponseDto> getMyStories(Long memberId, Pageable pageable, StorySearchRequestDto searchRequest) {
//        return storyQueryRepository.findMyStoryPages(memberId, pageable, searchRequest);
        return storyQueryRepository.findStoryPages(pageable, searchRequest, true, memberId);
    }

    @Transactional
    @Override
    public StoryDetailResponseDto getStory(Long storyId, CustomUserDetails userDetails) {
        Story story = storyRepository.findById(storyId).orElseThrow(()
                -> new StoryNotFoundException("존재하지 않는 동화입니다."));
        // 조회수 1 증가
        story.increaseViewCount(1);

        if (userDetails != null) {
            Long memberId = userDetails.getMemberId();
            boolean isLiked = isLiked(memberId, storyId);
            return new StoryDetailResponseDto(story, isLiked);
        }
        return new StoryDetailResponseDto(story);
    }

    @Transactional(readOnly = true)
    @Override
    public StoryDetailResponseDto getLatestStory(Long memberId) {
        Story latestStory = storyQueryRepository.findLatestStory(memberId);
        if (latestStory == null) {
            return null;
        }
        return new StoryDetailResponseDto(latestStory);
    }

    @Transactional(readOnly = true)
    @Override
    public void getMyDubbedStories(Long memberId, Pageable pageable) {

    }

    private boolean isLiked(Long memberId, Long storyId) {
        return storyLikeRepository.existsByMemberIdAndStoryId(memberId, storyId);
    }
}
