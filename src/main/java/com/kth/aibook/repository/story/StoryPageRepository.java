package com.kth.aibook.repository.story;

import com.kth.aibook.entity.story.StoryPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryPageRepository extends JpaRepository<StoryPage, Long> {
}
