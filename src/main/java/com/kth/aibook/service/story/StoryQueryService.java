package com.kth.aibook.service.story;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryQueryService {
    List<StorySimpleResponseDto> getMostViewedStories();
    List<StorySimpleResponseDto> getMostLikedStories();
    Page<StorySimpleResponseDto> getPublicStories(Pageable pageable);

    Page<StorySimpleResponseDto> getMyStories(Long memberId, Pageable pageable);

    StoryDetailResponseDto getStory(Long storyId, CustomUserDetails userDetails);
}
