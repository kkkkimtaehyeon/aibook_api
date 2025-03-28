package com.kth.aibook.entity.story;

import com.kth.aibook.entity.member.Voice;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

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

    private LocalDateTime dubbedAt;
}
