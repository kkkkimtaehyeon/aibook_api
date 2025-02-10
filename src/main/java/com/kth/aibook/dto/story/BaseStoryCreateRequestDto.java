package com.kth.aibook.dto.story;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record BaseStoryCreateRequestDto(@NotBlank @Length(max = 500) String baseStory) {
}
