package com.kth.aibook.exception.storyDubbing;

public class StoryDubbingNotFoundException extends RuntimeException {
    public StoryDubbingNotFoundException(Long storyDubbingId) {
        super("story dubbing not found! (storyDubbingId: " + storyDubbingId + ")");
    }
}
