package com.kth.aibook.repository.story;


import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.entity.story.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoryRepository extends JpaRepository<Story, Long> {
//    StoryDetailResponseDto findByIdWithLikeCount(@Param("storyId") Long storyId);
//    StoryDetailResponseDto findByIdWithLikeCount(@Param("storyId") Long storyId, @Param("memberId") Long memberId);

}
