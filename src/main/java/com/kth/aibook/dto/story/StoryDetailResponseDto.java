package com.kth.aibook.dto.story;

import com.kth.aibook.entity.story.Story;
import com.kth.aibook.entity.story.StoryPage;
import com.querydsl.core.annotations.QueryProjection;
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
    private List<StoryPageDto> pages;

    public StoryDetailResponseDto(Story story) {
        this.storyId = story.getId();
        this.title = story.getTitle();
        this.memberId = story.getMember().getId();
        this.author = story.getMember().getNickName();
        this.pages = story.getStoryPages().stream().map(StoryPageDto::new).toList();
    }
}

@NoArgsConstructor
@Getter
class StoryPageDto {
    private int pageNumber;
    private String content;

    public StoryPageDto(StoryPage storyPage) {
        this.pageNumber = storyPage.getPageNumber();
        this.content = storyPage.getContent();
    }
}


