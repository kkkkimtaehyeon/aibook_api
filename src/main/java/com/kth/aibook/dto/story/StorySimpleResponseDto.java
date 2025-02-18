package com.kth.aibook.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StorySimpleResponseDto {
    private Long storyId;
    private String title;
    private Long memberId;
    private String memberName;
    private Long viewCount;
    private Long likeCount;

    @QueryProjection
    public StorySimpleResponseDto(Long storyId, String title, Long memberId, String memberName, Long viewCount, Long likeCount) {
        this.storyId = storyId;
        this.title = title;
        this.memberId = memberId;
        this.memberName = memberName;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
