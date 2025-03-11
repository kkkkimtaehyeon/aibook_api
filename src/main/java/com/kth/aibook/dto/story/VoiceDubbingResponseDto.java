package com.kth.aibook.dto.story;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class VoiceDubbingResponseDto {
    private Map<Long, String> storyDubbingMap = new HashMap<>();
}
