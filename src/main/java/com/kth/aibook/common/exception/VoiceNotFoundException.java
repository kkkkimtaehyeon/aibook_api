package com.kth.aibook.common.exception;

public class VoiceNotFoundException extends RuntimeException {
    public VoiceNotFoundException(Long voiceId) {
        super("voice not found! (voiceId: " + voiceId + ")");
    }
}
