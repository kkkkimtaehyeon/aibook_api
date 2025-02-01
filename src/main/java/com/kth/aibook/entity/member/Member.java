package com.kth.aibook.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String nickName;

    private LocalDate birth;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private MemberRole role;


    @Builder
    public Member(Long id, String email, String nickName, LocalDate birth, LocalDateTime createdAt, MemberRole role) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.birth = birth;
        this.createdAt = createdAt;
        this.role = role;
    }
}
