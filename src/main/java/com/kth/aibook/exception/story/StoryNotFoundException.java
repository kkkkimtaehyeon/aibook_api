package com.kth.aibook.exception.story;

public class StoryNotFoundException extends RuntimeException {

    public StoryNotFoundException(String message) {
        super(message);
    }
}
