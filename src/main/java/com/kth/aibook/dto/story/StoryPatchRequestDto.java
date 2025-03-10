package com.kth.aibook.dto.story;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class StoryPatchRequestDto {
    @Nullable
    private String title;
}
