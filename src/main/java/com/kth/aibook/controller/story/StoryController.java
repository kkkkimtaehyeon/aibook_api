package com.kth.aibook.controller.story;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryCompleteRequestDto;
import com.kth.aibook.dto.story.StoryPageCreateRequestDto;
import com.kth.aibook.service.story.StoryLikeService;
import com.kth.aibook.service.story.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryController {
    private final StoryService storyService;
    private final StoryLikeService storyLikeService;

    // 동화 기초이야기 저장
    @PostMapping("/base-story")
    public ApiResponse<Long> createBaseStory(@AuthenticationPrincipal CustomUserDetails user,
                                             @Valid @RequestBody BaseStoryCreateRequestDto createRequest) {
        Long memberId = user.getMemberId();
        Long savedStoryId = storyService.createBaseStory(memberId, createRequest);

        return ApiResponse.success(HttpStatus.CREATED, savedStoryId);
    }

    // 동화 페이지 저장
    @PostMapping("/{story-id}/pages/{page-number}")
    public ApiResponse<Long> createStoryPage(@PathVariable("story-id") Long storyId,
                                             @PathVariable("page-number") Integer pageNumber,
                                             @RequestBody StoryPageCreateRequestDto createRequest) {
        createRequest.setPageNumber(pageNumber);
        Long savedStoryPageId = storyService.createStoryPage(storyId, createRequest);
        return ApiResponse.success(HttpStatus.CREATED, savedStoryPageId);
    }

    // 동화 완성
    @PatchMapping("/{story-id}/complete")
    public ApiResponse<Long> completeStory(@PathVariable("story-id") Long storyId,
                                           @RequestBody StoryCompleteRequestDto completeRequest) {
        Long completedStoryId = storyService.completeStory(storyId, completeRequest);
        return ApiResponse.success(HttpStatus.CREATED, completedStoryId);
    }

    // 동화 좋아요
    @PostMapping("/{story-id}/like")
    public ApiResponse<Void> likeStory(@PathVariable("story-id") Long storyId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        storyLikeService.likeStory(memberId, storyId);
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
