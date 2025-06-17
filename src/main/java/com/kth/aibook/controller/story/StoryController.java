package com.kth.aibook.controller.story;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.story.StoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryPatchRequestDto;
import com.kth.aibook.service.story.StoryLikeService;
import com.kth.aibook.service.story.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryController {
    private final StoryService storyService;
    private final StoryLikeService storyLikeService;

    // 동화 생성
    @PostMapping
    public ResponseEntity<ApiResponseBody> createStory(@AuthenticationPrincipal CustomUserDetails user,
                                                       @Valid @RequestBody StoryCreateRequestDto request) {
        Long memberId = user.getMemberId();
        Long savedStoryId = storyService.createStory(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseBody(savedStoryId));
    }

    // 동화 좋아요
    @PostMapping("/{story-id}/like")
    public ResponseEntity<Void> likeStory(@PathVariable("story-id") Long storyId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        storyLikeService.likeStory(memberId, storyId);
        return ResponseEntity.ok().build();
    }

    // 동화 삭제
    @DeleteMapping("/{story-id}")
    public ResponseEntity<Void> deleteStory(@PathVariable("story-id") Long storyId) {
        storyService.removeStory(storyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // 동화 수정
    @PatchMapping("/{story-id}")
    public ResponseEntity<Void> patchStory(@PathVariable("story-id") Long storyId,
                                     @RequestBody StoryPatchRequestDto patchRequest) {
        storyService.patchStory(storyId, patchRequest);
        return ResponseEntity.ok().build();
    }
}
