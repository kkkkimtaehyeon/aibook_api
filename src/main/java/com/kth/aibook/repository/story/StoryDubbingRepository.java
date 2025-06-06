package com.kth.aibook.repository.story;

import com.kth.aibook.entity.story.StoryDubbing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryDubbingRepository extends JpaRepository<StoryDubbing, Long> {
    @Query("SELECT sd " +
            "FROM StoryDubbing sd " +
            "WHERE sd.member.id = :memberId")
    List<StoryDubbing> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
