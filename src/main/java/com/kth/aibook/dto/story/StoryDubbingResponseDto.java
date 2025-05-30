package com.kth.aibook.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StoryDubbingResponseDto {
    private final Long id;
    private final String title;
    private final String writer;
    private final String voiceName;
    private final String coverImageUrl;

    @QueryProjection
    public StoryDubbingResponseDto(Long id, String title, String writer, String voiceName, String coverImageUrl) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.voiceName = voiceName;
        this.coverImageUrl = coverImageUrl;
    }
}
