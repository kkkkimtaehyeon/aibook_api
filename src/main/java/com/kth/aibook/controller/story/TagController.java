package com.kth.aibook.controller.story;

import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.story.tag.TagCreateDto;
import com.kth.aibook.dto.story.tag.TagDto;
import com.kth.aibook.service.story.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;


@RequiredArgsConstructor
@RequestMapping("/api/tags")
@Controller
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ApiResponseBody> getTags() {
        Set<TagDto> tags = tagService.getTags();
        return ResponseEntity.ok(new ApiResponseBody(tags));
    }

    @PostMapping
    public ResponseEntity<Void> createTag(TagCreateDto createDto) {
        tagService.createTag(createDto);
        return ResponseEntity.ok(null);
    }
}
