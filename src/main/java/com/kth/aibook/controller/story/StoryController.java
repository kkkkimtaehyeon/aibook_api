package com.kth.aibook.controller.story;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.dto.story.*;
import com.kth.aibook.service.story.StoryDubbingService;
import com.kth.aibook.service.story.StoryLikeService;
import com.kth.aibook.service.story.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryController {
    private final StoryService storyService;
    private final StoryLikeService storyLikeService;

    // 동화 생성
    @PostMapping
    public ApiResponse<Long> createStory(@AuthenticationPrincipal CustomUserDetails user,
                                         @Valid @RequestBody StoryCreateRequestDto request) {
        Long memberId = user.getMemberId();
        Long createdStoryId = storyService.createStory(memberId, request);
        return ApiResponse.success(HttpStatus.CREATED, createdStoryId);
    }

    // 동화 좋아요
    @PostMapping("/{story-id}/like")
    public ApiResponse<Void> likeStory(@PathVariable("story-id") Long storyId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        storyLikeService.likeStory(memberId, storyId);
        return ApiResponse.success(HttpStatus.OK, null);
    }

    // 동화 삭제
    @DeleteMapping("/{story-id}")
    public ApiResponse<Void> deleteStory(@PathVariable("story-id") Long storyId) {
        storyService.removeStory(storyId);
        return ApiResponse.success(HttpStatus.NO_CONTENT, null);
    }

    // 동화 수정
    @PatchMapping("/{story-id}")
    public ApiResponse<?> patchStory(@PathVariable("story-id") Long storyId,
                                     @RequestBody StoryPatchRequestDto patchRequest) {
        storyService.patchStory(storyId, patchRequest);
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
