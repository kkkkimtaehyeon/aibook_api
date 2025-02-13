package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import com.kth.aibook.entity.story.Story;
import com.kth.aibook.exception.story.StoryNotFoundException;
import com.kth.aibook.repository.story.StoryQueryRepository;
import com.kth.aibook.repository.story.StoryRepository;
import com.kth.aibook.service.story.StoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoryQueryServiceImpl implements StoryQueryService {
    private final StoryQueryRepository storyQueryRepository;
    private final StoryRepository storyRepository;

    @Override
    public Page<StorySimpleResponseDto> getPublicStories(Pageable pageable) {
        return storyQueryRepository.findStoryPages(pageable, true);
    }

    @Override
    public Page<StorySimpleResponseDto> getMyStories(Long memberId, Pageable pageable) {
        return storyQueryRepository.findMyStoryPages(memberId, pageable);
    }

    @Override
    public StoryDetailResponseDto getStory(Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(()
                -> new StoryNotFoundException("존재하지 않는 동화입니다."));
        return new StoryDetailResponseDto(story);
    }
}
