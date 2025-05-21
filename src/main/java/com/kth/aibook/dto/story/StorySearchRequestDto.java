package com.kth.aibook.dto.story;

import jakarta.annotation.Nullable;

public record StorySearchRequestDto (@Nullable String searchTarget,
                                     @Nullable String searchKey,
                                     @Nullable String sortBy,
                                     @Nullable String sortDir,
                                     @Nullable Long tagId){
}
