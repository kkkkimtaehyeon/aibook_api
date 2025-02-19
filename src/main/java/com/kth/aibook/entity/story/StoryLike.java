package com.kth.aibook.entity.story;

import com.kth.aibook.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class StoryLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "story_id")
    private Story story;

    public StoryLike(Member member, Story story) {
        this.member = member;
        this.story = story;
    }
}
