package com.kth.aibook.entity.story;

import com.kth.aibook.entity.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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

    @Column(length = 500, nullable = true)
    private String title;

    @Column(columnDefinition = "DATETIME", nullable = true)
    private LocalDateTime createdAt;

    @ColumnDefault(value = "false")
    @Column(columnDefinition = "TINYINT", nullable = true)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Story(Long id, String baseStory, String title, LocalDateTime createdAt, boolean isPublic, Member member) {
        this.id = id;
        this.baseStory = baseStory;
        this.title = title;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.member = member;
    }
}
