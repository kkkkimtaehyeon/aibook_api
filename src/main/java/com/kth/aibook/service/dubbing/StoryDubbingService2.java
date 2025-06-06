package com.kth.aibook.service.dubbing;

import com.kth.aibook.dto.dubbedStory.DubbedStoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryDubbingResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryDubbingService2 {
    void requestDubbedStory(Long memberId, DubbedStoryCreateRequestDto request);
    Long saveDubbedStory(Long storyId, Long memberId, Long voiceId, String requestId);
    List<StoryDubbingResponseDto> getDubbedStories(Long memberId, Pageable pageable);
    void removeStoryDubbing(Long storyDubbingId);

    StoryDubbingResponseDto getDubbedStory(Long dubbedStoryId);


}
