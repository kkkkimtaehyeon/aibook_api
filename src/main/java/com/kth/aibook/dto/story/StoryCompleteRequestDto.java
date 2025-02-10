package com.kth.aibook.dto.story;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoryCompleteRequestDto (@NotBlank String title,
                                       @NotNull Boolean isPublic) {

}
