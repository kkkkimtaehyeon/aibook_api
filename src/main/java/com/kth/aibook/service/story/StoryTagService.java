package com.kth.aibook.service.story;


import com.kth.aibook.dto.story.storyTag.StoryTagDto;

import java.util.List;

public interface StoryTagService {
    List<StoryTagDto> getStoryTags(Long storyId);
    void addStoryTags(Long storyId, List<Long> tagIds);
}
