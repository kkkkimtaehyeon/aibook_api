package com.kth.aibook.controller.story;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.dubbedStory.DubbedStoryCreateRequestDto;
import com.kth.aibook.dto.story.StoryDubbingResponseDto;
import com.kth.aibook.service.dubbing.StoryDubbingService2;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoryDubbingController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StoryDubbingController.class);
    private final StoryDubbingService2 storyDubbingService2;


    // 동화 더빙 생성
    @PostMapping("/api/dubbed-stories")
    public ResponseEntity<Void> createdDubbedStory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody @Valid DubbedStoryCreateRequestDto request) {
        Long memberId = userDetails.getMemberId();
        storyDubbingService2.requestDubbedStory(memberId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 동화 더빙 완료 webhook
    @PostMapping("/api/stories/{story-id}/members/{member-id}/voices/{voice-id}/dubbing/completed")
    public ResponseEntity<?> completeVoiceDubbing(@PathVariable("story-id") Long storyId,
                                                  @PathVariable("member-id") Long memberId,
                                                  @PathVariable("voice-id") Long voiceId,
                                                  @RequestParam("request-id") String requestId) {
        storyDubbingService2.saveDubbedStory(storyId, memberId, voiceId, requestId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // 더빙한 동화 목록 조회
    @GetMapping("/api/dubbed-stories")
    public ResponseEntity<?> getDubbedStories() {
        List<StoryDubbingResponseDto> dubbedStories = storyDubbingService2.getDubbedStories(3L, null);
        return ResponseEntity.ok(new ApiResponseBody(dubbedStories));
    }

    // 더빙한 동화 상세 조회
    @GetMapping("/api/dubbed-stories/{dubbed-story-id}")
    public ResponseEntity<ApiResponseBody> getDubbedStory(@PathVariable("dubbed-story-id") Long dubbedStoryId) {
        StoryDubbingResponseDto dubbedStory = storyDubbingService2.getDubbedStory(dubbedStoryId);
        return ResponseEntity.ok(new ApiResponseBody(dubbedStory));
    }

    // 더빙한 동화 삭제
    @DeleteMapping("/api/dubbed-stories/{story-dubbing-id}")
    public ResponseEntity<Void> removeStoryDubbing(@PathVariable("story-dubbing-id") Long storyDubbingId) {
        storyDubbingService2.removeStoryDubbing(storyDubbingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
