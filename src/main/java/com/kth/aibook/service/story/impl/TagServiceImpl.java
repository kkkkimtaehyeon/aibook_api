package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.tag.TagCreateDto;
import com.kth.aibook.dto.story.tag.TagDto;
import com.kth.aibook.entity.story.Tag;
import com.kth.aibook.repository.story.TagRepository;
import com.kth.aibook.service.story.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Set<TagDto> getTags() {
        return tagRepository.findAllTags();
    }

    @Override
    public void createTag(TagCreateDto createDto) {
        if (tagRepository.existsByName(createDto.name())) {
            throw new RuntimeException("이미 등록된 태그입니다.");
        }
        tagRepository.save(createDto.toEntity());
    }
}
