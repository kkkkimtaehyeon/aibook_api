package com.kth.aibook.controller.story;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.story.*;
import com.kth.aibook.service.story.StoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryQueryController {
    //    private static final Logger log = LoggerFactory.getLogger(StoryQueryController.class);
    private final StoryQueryService storyQueryService;

    @GetMapping("/most-viewed")
    public ResponseEntity<ApiResponseBody> getMostViewedStories() {
        List<StorySimpleResponseDto> mostViewedStories = storyQueryService.getMostViewedStories();
        return ResponseEntity.ok().body(new ApiResponseBody(mostViewedStories));
    }

    @GetMapping("/most-liked")
    public ResponseEntity<ApiResponseBody> getMostLikedStories() {
        List<StorySimpleResponseDto> mostLikedStories = storyQueryService.getMostLikedStories();
        return ResponseEntity.ok().body(new ApiResponseBody(mostLikedStories));
    }

    // 동화 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponseBody> getStories(Pageable pageable,
                                                      StorySearchRequestDto searchRequest) {
        Page<StorySimpleResponseDto> stories = storyQueryService.getPublicStories(pageable, searchRequest);
        return ResponseEntity.ok(new ApiResponseBody(stories));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponseBody> getMyStories(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PageableDefault(size = 10) Pageable pageable,
                                                        StorySearchRequestDto searchRequest) {
        // 유저정보 못가져올 때
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseBody("login required", null));
        }
        Long memberId = userDetails.getMemberId();
        Page<StorySimpleResponseDto> storyPages = storyQueryService.getMyStories(memberId, pageable, searchRequest);
        return ResponseEntity.ok(new ApiResponseBody(storyPages));
    }

    // 동화 상세조회
    @GetMapping("/{story-id}")
    public ResponseEntity<ApiResponseBody> getStory(@PathVariable("story-id") Long storyId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 로그인한 회원이면 좋아요 정보도 같이 가져옴
        StoryDetailResponseDto storyDetail = storyQueryService.getStory(storyId, userDetails);
        return ResponseEntity.ok(new ApiResponseBody(null, storyDetail));
    }


    // 최신
    @GetMapping("/my/latest")
    public ResponseEntity<ApiResponseBody> getMyLatestStory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMemberId();
        StoryDetailResponseDto latestStoryDetail = storyQueryService.getLatestStory(memberId);
        return ResponseEntity.ok(new ApiResponseBody(latestStoryDetail));
    }

    // 내가 더빙한 동화 목록 조회
    @GetMapping("/my/dubbed-stories")
    public ResponseEntity<ApiResponseBody> getMyDubbedStories(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PageableDefault(size = 10) Pageable pageable) {
        Long memberId = userDetails.getMemberId();
        Page<StoryDubbingResponseDto> storyDubbingList = storyQueryService.getMyDubbedStories(memberId, pageable);
        return ResponseEntity.ok(new ApiResponseBody(storyDubbingList));
    }

    @GetMapping("/dubbed-stories/{dubbed-story-id}")
    public ResponseEntity<ApiResponseBody> getDubbedStory(@PathVariable("dubbed-story-id") Long storyDubbingId) {
        StoryDubbingDetailResponseDto response = storyQueryService.getStoryDubbing(storyDubbingId);
        return ResponseEntity.ok(new ApiResponseBody(response));
    }
}
