package com.kth.aibook.common.exception;

public class StoryDubbingException extends RuntimeException {
    public StoryDubbingException(String message) {
        super(message);
    }

    public StoryDubbingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoryDubbingException(Throwable cause) {
        super(cause);
    }
}
