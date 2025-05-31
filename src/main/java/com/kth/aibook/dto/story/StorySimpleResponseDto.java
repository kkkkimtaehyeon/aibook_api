package com.kth.aibook.dto.story;

import com.kth.aibook.dto.story.tag.TagDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
public class StorySimpleResponseDto {
    private Long storyId;
    private String title;
    private Long writerId;
    private String writer;
    private Long viewCount;
    private Long likeCount;
    private String coverImageUrl;
    @Setter
    private List<TagDto> tags;

    @QueryProjection
    public StorySimpleResponseDto(Long storyId, String title, Long writerId, String writer, Long viewCount, Long likeCount, String coverImageUrl) {
        this.storyId = storyId;
        this.title = title;
        this.writerId = writerId;
        this.writer = writer;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.coverImageUrl = coverImageUrl;
    }

    @QueryProjection
    public StorySimpleResponseDto(Long storyId, String title, Long writerId, String writer, Long viewCount, Long likeCount, List<TagDto> tags, String coverImageUrl) {
        this.storyId = storyId;
        this.title = title;
        this.writerId = writerId;
        this.writer = writer;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.tags = tags;
        this.coverImageUrl = coverImageUrl;
    }
}
