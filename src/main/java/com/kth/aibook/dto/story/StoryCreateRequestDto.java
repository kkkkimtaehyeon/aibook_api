package com.kth.aibook.dto.story;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record StoryCreateRequestDto(
        @NotBlank String title,
        @NotBlank String baseStory,
        @NotEmpty String[] selectedSentences
) {

}
