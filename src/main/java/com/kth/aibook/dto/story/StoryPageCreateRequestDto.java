package com.kth.aibook.dto.story;

import com.kth.aibook.entity.story.StoryPage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StoryPageCreateRequestDto {
    @Setter
    private int pageNumber;

    @NotBlank
    private String selectedContent;

    public StoryPage toEntity() {
        return StoryPage.builder()
                .pageNumber(pageNumber)
                .content(selectedContent)
                .build();
    }
}
