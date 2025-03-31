package com.kth.aibook.entity.story;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class StoryPageDubbing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_page_dubbing_id", nullable = false)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "story_dubbing_id", nullable = false)
    private StoryDubbing storyDubbing;

    @ManyToOne
    @JoinColumn(name = "story_page_id", nullable = false)
    private StoryPage storyPage;

    @Column(nullable = false)
    private String dubbingAudioUrl;

    @Builder
    public StoryPageDubbing(Long id, StoryDubbing storyDubbing, StoryPage storyPage, String dubbingAudioUrl) {
        this.id = id;
        this.storyDubbing = storyDubbing;
        this.storyPage = storyPage;
        this.dubbingAudioUrl = dubbingAudioUrl;
    }

    public StoryPageDubbing(StoryPage storyPage, String dubbingAudioUrl) {
        this.storyPage = storyPage;
        this.dubbingAudioUrl = dubbingAudioUrl;
    }
}
