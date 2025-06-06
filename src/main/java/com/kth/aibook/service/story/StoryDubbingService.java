package com.kth.aibook.service.story;

public interface StoryDubbingService {

    void requestDubbing(Long storyId, Long voiceId, Long memberId);

    void saveDubbing(Long storyId, Long memberId, Long voiceId, String requestId);

    void deleteStoryDubbing(Long storyDubbingId);

}
