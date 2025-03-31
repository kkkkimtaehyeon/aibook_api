package com.kth.aibook.service.story;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.dto.story.StoryDubbingResponseDto;
import com.kth.aibook.dto.story.StorySearchRequestDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryQueryService {
    List<StorySimpleResponseDto> getMostViewedStories();

    List<StorySimpleResponseDto> getMostLikedStories();

    Page<StorySimpleResponseDto> getPublicStories(Pageable pageable, StorySearchRequestDto searchRequest);

    Page<StorySimpleResponseDto> getMyStories(Long memberId, Pageable pageable, StorySearchRequestDto searchRequest);

    StoryDetailResponseDto getStory(Long storyId, CustomUserDetails userDetails);

    StoryDetailResponseDto getLatestStory(Long memberId);

    Page<StoryDubbingResponseDto> getMyDubbedStories(Long memberId, Pageable pageable);
}
