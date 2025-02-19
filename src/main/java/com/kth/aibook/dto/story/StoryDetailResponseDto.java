package com.kth.aibook.dto.story;

import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryPage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class StoryDetailResponseDto {
    private Long storyId;
    private String title;
    private Long memberId;
    private String author;
    private Long viewCount;
    private List<StoryPageDto> pages;
    private boolean isLiked = false;

    public StoryDetailResponseDto(Story story) {
        this.storyId = story.getId();
        this.title = story.getTitle();
        this.memberId = story.getMember().getId();
        this.viewCount = story.getViewCount();
        this.author = story.getMember().getNickName();
        this.pages = story.getStoryPages().stream().map(StoryPageDto::new).toList();
    }

    public StoryDetailResponseDto(Story story, boolean isLiked) {
        this.storyId = story.getId();
        this.title = story.getTitle();
        this.memberId = story.getMember().getId();
        this.author = story.getMember().getNickName();
        this.viewCount = story.getViewCount();
        this.pages = story.getStoryPages().stream().map(StoryPageDto::new).toList();
        this.isLiked = isLiked;
    }
}

@NoArgsConstructor
@Getter
class StoryPageDto {
    private int pageNumber;
    private String content;
    private String dubbingAudioUrl;

    public StoryPageDto(StoryPage storyPage) {
        this.pageNumber = storyPage.getPageNumber();
        this.content = storyPage.getContent();
        this.dubbingAudioUrl = storyPage.getDubbingAudioUrl();
    }
}


