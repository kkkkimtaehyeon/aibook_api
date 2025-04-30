package com.kth.aibook.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Voice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String audioUrl;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Voice(Long id, String name, String audioUrl, Member member, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.audioUrl = audioUrl;
        this.deletedAt = deletedAt;
        this.member = member;
    }

    public void logicallyDeleteVoice() {
        this.deletedAt = LocalDateTime.now();
    }
}
