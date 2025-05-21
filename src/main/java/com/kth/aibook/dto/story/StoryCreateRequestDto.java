package com.kth.aibook.dto.story;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.story.Story;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record StoryCreateRequestDto(
        @NotBlank String title,
        @NotBlank String baseStory,
        @NotEmpty @Length(min = 10, max = 10) String[] selectedSentences,
        @NotNull Boolean isPublic,
        @Nullable long[] tagIds
) {
    public Story toEntity(Member member, LocalDateTime createdAt) {
        return Story.builder()
                .baseStory(baseStory)
                .createdAt(createdAt)
                .member(member)
                .title(title)
                .isPublic(isPublic)
                .isDubbed(false)
                .build();
    }
}
