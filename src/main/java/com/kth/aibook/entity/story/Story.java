package com.kth.aibook.entity.story;

import com.kth.aibook.dto.story.StoryCompleteRequestDto;
import com.kth.aibook.entity.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id", nullable = false)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String baseStory;

    @Column(length = 100, nullable = true)
    private String title;

    @Column(columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime createdAt;

    @ColumnDefault(value = "false")
    @Column(columnDefinition = "TINYINT", nullable = true)
    private boolean isPublic;

    @ColumnDefault(value = "false")
    @Column(columnDefinition = "TINYINT", nullable = true)
    private boolean isDubbed;

    @ColumnDefault(value = "0")
    @Column(columnDefinition = "BIGINT", nullable = false)
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StoryPage> storyPages = new ArrayList<>();

    @Builder
    public Story(Long id, String baseStory, String title, LocalDateTime createdAt, boolean isPublic, boolean isDubbed, Member member) {
        this.id = id;
        this.baseStory = baseStory;
        this.title = title;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.isDubbed = isDubbed;
        this.member = member;
    }

    public void addStoryPage(StoryPage storyPage) {
        storyPage.setStory(this);
        this.storyPages.add(storyPage);
    }

    public void completeStory(StoryCompleteRequestDto completeRequest) {
        this.title = completeRequest.title();
        this.isPublic = completeRequest.isPublic();
        this.createdAt = LocalDateTime.now();
    }

    public void completeDubbing() {
        this.isDubbed = true;
    }

    public void increaseViewCount(int increaseViewCount) {
        this.viewCount += increaseViewCount;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
