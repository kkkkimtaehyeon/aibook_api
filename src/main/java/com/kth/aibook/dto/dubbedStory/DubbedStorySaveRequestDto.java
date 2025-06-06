package com.kth.aibook.dto.dubbedStory;

import java.util.Map;

public record DubbedStorySaveRequestDto(Map<Long, String> dubbingMap) {
}
