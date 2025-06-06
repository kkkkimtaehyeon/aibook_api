package com.kth.aibook.repository.member;

import com.kth.aibook.dto.member.VoiceDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<VoiceDto> findByMember(Member member);

    @Query("SELECT new com.kth.aibook.dto.member.VoiceDto(v.id, v.name) " +
            "FROM Voice v " +
            "INNER JOIN Member m ON v.member.id = m.id " +
            "WHERE m.id = :memberId AND v.deletedAt IS NULL ")
    List<VoiceDto> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT new com.kth.aibook.dto.member.VoiceDto(v.id, v.name) " +
            "FROM Voice v " +
            "WHERE v.isDefault = true")
    List<VoiceDto> findDefaultVoices();


}
