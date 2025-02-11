package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryQueryService {
    Page<StorySimpleResponseDto> getStories(Pageable pageable);
    StoryDetailResponseDto getStory(Long storyId);
}
