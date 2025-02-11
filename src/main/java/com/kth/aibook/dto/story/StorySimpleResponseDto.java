package com.kth.aibook.dto.story;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StorySimpleResponseDto {
    private Long storyId;
    private String title;
    private Long memberId;
    private String memberName;
}
