package com.kth.aibook.repository.member;

import com.kth.aibook.dto.member.VoiceDto;
import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.Voice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<VoiceDto> findByMember(Member member);
}
