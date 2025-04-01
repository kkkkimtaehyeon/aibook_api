package com.kth.aibook.dto.story;

import com.kth.aibook.entity.story.StoryDubbing;
import com.kth.aibook.entity.story.StoryPageDubbing;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StoryDubbingDetailResponseDto {
    private final Long id;
    private final String title;
    private final String author;
    private final List<StoryPageDubbingDto> pages = new ArrayList<>();

    public StoryDubbingDetailResponseDto(StoryDubbing storyDubbing) {
        this.id = storyDubbing.getId();
        this.title = storyDubbing.getStory().getTitle();
        this.author = storyDubbing.getStory().getMember().getNickName();
        for (StoryPageDubbing page: storyDubbing.getStoryPageDubbings()) {
            this.pages.add(new StoryPageDubbingDto(page));
        }
    }
}

@Getter
class StoryPageDubbingDto {
    private final int pageNumber;
    private final String content;
    private final String audioUrl;

    public StoryPageDubbingDto (StoryPageDubbing storyPageDubbing) {
        this.pageNumber = storyPageDubbing.getStoryPage().getPageNumber();
        this.content = storyPageDubbing.getStoryPage().getContent();
        this.audioUrl = storyPageDubbing.getDubbingAudioUrl();
    }
}


