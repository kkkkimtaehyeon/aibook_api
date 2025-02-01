package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;

public interface StoryService {
    Long createBaseStory (long memberId, BaseStoryCreateRequestDto createRequest);
}
