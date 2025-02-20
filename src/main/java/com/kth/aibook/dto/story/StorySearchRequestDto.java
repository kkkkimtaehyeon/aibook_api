package com.kth.aibook.dto.story;

import jakarta.annotation.Nullable;

public record StorySearchRequestDto (@Nullable String searchTarget,
                                     @Nullable String searchKey){
}
