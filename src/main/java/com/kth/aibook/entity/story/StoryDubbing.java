package com.kth.aibook.entity.story;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class StoryDubbing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_dubbing_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @ManyToOne
    @JoinColumn(name = "voice_id", nullable = false)
    private Voice voice;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "storyDubbing", fetch = FetchType.LAZY)
    private List<StoryPageDubbing> storyPageDubbings = new ArrayList<>();

    private LocalDateTime dubbedAt;

    @Builder
    public StoryDubbing(Long id, Story story, Voice voice, Member member, LocalDateTime dubbedAt) {
        this.id = id;
        this.story = story;
        this.voice = voice;
        this.member = member;
        this.dubbedAt = dubbedAt;
    }

    public void addStoryDubbingPage(StoryPageDubbing storyPageDubbing) {
        storyPageDubbing.setStoryDubbing(this);
        getStoryPageDubbings().add(storyPageDubbing);
    }
}
