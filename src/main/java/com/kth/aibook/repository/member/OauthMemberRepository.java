package com.kth.aibook.repository.member;


import com.kth.aibook.entity.member.OauthMember;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OauthMemberRepository extends JpaRepository<OauthMember, Long> {

    @Query("SELECT om FROM OauthMember om WHERE om.provider = :provider AND om.provider_member_id = :providerMemberId")
    Optional<OauthMember> findOauthMember(@Param("provider") String provider,
                                          @Param("providerMemberId") long providerMemberId);
}
