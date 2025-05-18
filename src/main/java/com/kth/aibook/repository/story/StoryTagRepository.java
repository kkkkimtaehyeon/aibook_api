package com.kth.aibook.repository.story;

import com.kth.aibook.entity.story.StoryTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryTagRepository extends JpaRepository<StoryTag, Long> {
}
