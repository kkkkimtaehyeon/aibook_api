package com.kth.aibook.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Voice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_id")
    private Long id;

    private String name;

    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Voice(Long id, String name, String audioUrl, Member member) {
        this.id = id;
        this.name = name;
        this.audioUrl = audioUrl;
        this.member = member;
    }
}
