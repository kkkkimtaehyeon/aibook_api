package com.kth.aibook.dto.story;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StoryDubbingResponseDto {
    private Long id;
    private String title;
    private String voiceName;

    @QueryProjection
    public StoryDubbingResponseDto(Long id, String title, String voiceName) {
        this.id = id;
        this.title = title;
        this.voiceName = voiceName;
    }
}
