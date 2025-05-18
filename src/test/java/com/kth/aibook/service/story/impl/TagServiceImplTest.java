package com.kth.aibook.service.story.impl;

import com.kth.aibook.dto.story.tag.TagCreateDto;
import com.kth.aibook.dto.story.tag.TagDto;
import com.kth.aibook.entity.story.Tag;
import com.kth.aibook.repository.story.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.yml")
class TagServiceImplTest {

    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.save(new Tag("액션"));
        tagRepository.save(new Tag("로맨스"));
    }

    @AfterEach
    void tearDown() {
        tagRepository.deleteAll();
    }

    @Test
    void getTags() {
        Set<TagDto> tags = tagService.getTags();
        assertThat(tags.size()).isEqualTo(2);
    }

    @DisplayName("태그 추가")
    @Test
    void createTag() {
        TagCreateDto createDto = new TagCreateDto("코디미");
        tagService.createTag(createDto);

        Set<TagDto> tags = tagService.getTags();
        assertThat(tags.size()).isEqualTo(3);
    }

    @DisplayName("태그 중복 추가")
    @Test
    void createTag_duplicated() {
        TagCreateDto createDto = new TagCreateDto("액션");
        assertThatThrownBy(() -> {
            tagService.createTag(createDto);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("이미 등록된 태그입니다.");

        Set<TagDto> tags = tagService.getTags();
        assertThat(tags.size()).isEqualTo(2);
    }
}