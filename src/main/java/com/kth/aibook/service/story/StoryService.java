package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.*;

public interface StoryService {

    void removeStory(Long storyId);

    void patchStory(Long storyId, StoryPatchRequestDto patchRequest);

    Long createStory(Long memberId, StoryCreateRequestDto request);

}
