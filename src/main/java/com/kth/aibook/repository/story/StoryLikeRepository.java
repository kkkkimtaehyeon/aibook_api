package com.kth.aibook.repository.story;


import com.kth.aibook.entity.story.StoryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    Boolean existsByMemberIdAndStoryId(Long memberId, Long storyId);

    @Modifying
    @Query("DELETE FROM StoryLike sl WHERE sl.story.id = :storyId AND sl.member.id = :memberId")
    int deleteByMemberIdAndStoryId(@Param("memberId") Long memberId, @Param("storyId") Long storyId);

}

