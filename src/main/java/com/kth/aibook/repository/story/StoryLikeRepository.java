package com.kth.aibook.repository.story;


import com.kth.aibook.entity.story.StoryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    Boolean existsByMemberIdAndStoryId(Long memberId, Long storyId);

    @Modifying
    @Transactional
    void deleteByMemberIdAndStoryId(Long memberId, Long storyId);

}

