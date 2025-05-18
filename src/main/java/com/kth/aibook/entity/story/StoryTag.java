package com.kth.aibook.entity.story;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class StoryTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_tag_id", nullable = false)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public StoryTag(Tag tag) {
        this.tag = tag;
    }
}
