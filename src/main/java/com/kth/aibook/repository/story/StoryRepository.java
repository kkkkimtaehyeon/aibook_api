package com.kth.aibook.repository.story;


import com.kth.aibook.entity.story.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
