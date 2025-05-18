package com.kth.aibook.dto.story.tag;

import com.kth.aibook.entity.story.Tag;
import jakarta.validation.constraints.NotNull;

public record TagCreateDto(@NotNull String name) {
    public Tag toEntity() {
        return new Tag(name);
    }
}
