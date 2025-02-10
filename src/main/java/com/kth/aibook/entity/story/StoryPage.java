package com.kth.aibook.entity.story;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class StoryPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_page_id")
    private Long id;

    @Column(nullable = false)
    private Integer pageNumber;

    @Column(nullable = false, length = 300)
    private String content;

    @Setter
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @Builder
    public StoryPage(Long id, Integer pageNumber, String content, Story story) {
        this.id = id;
        this.pageNumber = pageNumber;
        this.content = content;
        this.story = story;
    }
}
