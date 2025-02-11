package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryCompleteRequestDto;
import com.kth.aibook.dto.story.StoryPageCreateRequestDto;

public interface StoryService {
    Long createBaseStory(long memberId, BaseStoryCreateRequestDto createRequest);

    Long createStoryPage(Long storyId, StoryPageCreateRequestDto createRequest);

    Long completeStory(Long storyId, StoryCompleteRequestDto completeRequest);

}
