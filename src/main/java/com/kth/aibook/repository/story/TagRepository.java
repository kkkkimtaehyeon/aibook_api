package com.kth.aibook.repository.story;


import com.kth.aibook.dto.story.tag.TagDto;
import com.kth.aibook.entity.story.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT new com.kth.aibook.dto.story.tag.TagDto(t.id, t.name) FROM Tag t")
    Set<TagDto> findAllTags();
    boolean existsByName(String name);
}
