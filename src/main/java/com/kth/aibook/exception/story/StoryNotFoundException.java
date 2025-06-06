package com.kth.aibook.exception.story;

public class StoryNotFoundException extends RuntimeException {

    public StoryNotFoundException(Long storyId) {
        super("story not found! (storyId: " + storyId + ")");
    }
}
