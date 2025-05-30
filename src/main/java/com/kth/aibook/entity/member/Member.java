package com.kth.aibook.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private LocalDate birthDate;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Setter
    @OneToOne
    @JoinColumn(name = "oauth_member_id")
    private OauthMember oauthMember;


    @Builder
    public Member(Long id, String email, String nickName, LocalDate birthDate, LocalDateTime createdAt, MemberRole role) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.role = role;
    }

    public Member(String email, String nickName, LocalDate birthDate, LocalDateTime createdAt, MemberRole role, OauthMember oauthMember) {
        this.email = email;
        this.nickName = nickName;
        this.birthDate = birthDate;
        this.createdAt = createdAt;
        this.role = role;
        this.oauthMember = oauthMember;
    }
}
