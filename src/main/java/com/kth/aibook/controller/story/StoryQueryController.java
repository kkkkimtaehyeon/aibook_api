package com.kth.aibook.controller.story;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.story.StoryDetailResponseDto;
import com.kth.aibook.dto.story.StorySimpleResponseDto;
import com.kth.aibook.service.story.StoryQueryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryQueryController {
    private static final Logger log = LoggerFactory.getLogger(StoryQueryController.class);
    private final StoryQueryService storyQueryService;

    // 동화목록 조회
    @GetMapping
    public ApiResponse<Page<StorySimpleResponseDto>> getStories(Pageable pageable) {
        Page<StorySimpleResponseDto> storyPages = storyQueryService.getPublicStories(pageable);
        return ApiResponse.success(HttpStatus.OK, storyPages);
    }

    // 동화 상세조회
    @GetMapping("/{story-id}")
    public ApiResponse<StoryDetailResponseDto> getStory(@PathVariable("story-id") Long storyId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인한 회원이면 좋아요 정보도 같이 가져옴
        StoryDetailResponseDto storyDetail;
        if (userDetails != null) {

        }
        storyDetail = storyQueryService.getStory(storyId);
        return ApiResponse.success(HttpStatus.OK, storyDetail);
    }

    @GetMapping("/my")
    public ApiResponse<Page<StorySimpleResponseDto>> getMyStories(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  Pageable pageable) {
        Long memberId = userDetails.getMemberId();
        Page<StorySimpleResponseDto> storyPages = storyQueryService.getMyStories(memberId, pageable);
        return ApiResponse.success(HttpStatus.OK, storyPages);
    }
}
