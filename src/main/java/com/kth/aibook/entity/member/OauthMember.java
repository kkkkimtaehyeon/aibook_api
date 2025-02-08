package com.kth.aibook.entity.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OauthMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_member_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String provider;

    @Column(nullable = false)
    private Long provider_member_id;

    public OauthMember(String provider, Long provider_member_id) {
        this.provider = provider;
        this.provider_member_id = provider_member_id;
    }
}
