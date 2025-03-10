package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryCompleteRequestDto;
import com.kth.aibook.dto.story.StoryPageCreateRequestDto;
import com.kth.aibook.dto.story.StoryPatchRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryService {
    Long createBaseStory(long memberId, BaseStoryCreateRequestDto createRequest);

    Long createStoryPage(Long storyId, StoryPageCreateRequestDto createRequest);

    Long completeStory(Long storyId, StoryCompleteRequestDto completeRequest);

    void removeStory(Long storyId);

    void addDubbings(Long storyId, List<MultipartFile> files);

    void patchStory(Long storyId, StoryPatchRequestDto patchRequest);

}
