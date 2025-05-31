package com.kth.aibook.dto.story;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.story.Story;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record StoryCreateRequestDto(
        @NotBlank String title,
        @NotBlank String baseStory,
        @NotNull @Size(min = 10, max = 10) String[] selectedSentences,
        @NotNull Boolean isPublic,
        @Nullable long[] tagIds,
        @NotNull String coverImageBase64
) {
    public Story toEntity(Member member, String coverImageUrl, LocalDateTime createdAt) {
        return Story.builder()
                .coverImageUrl(coverImageUrl)
                .baseStory(baseStory)
                .createdAt(createdAt)
                .member(member)
                .title(title)
                .isPublic(isPublic)
                .isDubbed(false)
                .build();
    }
}
