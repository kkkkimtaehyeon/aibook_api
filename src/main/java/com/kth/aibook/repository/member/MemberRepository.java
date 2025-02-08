package com.kth.aibook.repository.member;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.OauthMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByOauthMember(OauthMember oauthMember);
}
