package com.kth.aibook.dto.story.tag;//package com.kth.aibook.dto.story.tag;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class TagDto {
    private final Long id;
    private final String name;

    @QueryProjection
    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}