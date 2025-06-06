package com.kth.aibook.dto.story;

import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryDubbing;
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

    public StoryDubbingResponseDto(StoryDubbing storyDubbing) {
        Story story = storyDubbing.getStory();
        this.id = storyDubbing.getId();
        this.title = story.getTitle();
        this.writer = story.getMember().getNickName();
        this.voiceName = storyDubbing.getVoice().getName();
        this.coverImageUrl = story.getCoverImageUrl();

    }
}
