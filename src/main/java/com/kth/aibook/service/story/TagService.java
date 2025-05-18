package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.tag.TagCreateDto;
import com.kth.aibook.dto.story.tag.TagDto;

import java.util.Set;

public interface TagService {
    Set<TagDto> getTags();

    void createTag(TagCreateDto createDto);

}
