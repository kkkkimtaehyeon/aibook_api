package com.kth.aibook.controller.story;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.service.story.StoryDubbingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v2/stories")
@RestController
public class StoryDubbingControllerV2 {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StoryDubbingControllerV2.class);
    private final StoryDubbingService storyDubbingService;
    long start;
    long end;

    // 보이스 클로닝 더빙 요청
    @PostMapping("/{story-id}/voices/{voice-id}/dubbing")
    public ApiResponse<?> requestVoiceDubbing(@PathVariable("story-id") Long storyId,
                                              @PathVariable("voice-id") Long voiceId,
                                              @AuthenticationPrincipal CustomUserDetails userDetail) {
        start = System.currentTimeMillis();
        log.info("dubbing requested at {}", start);

        Long memberId = userDetail.getMemberId();
        try {
            storyDubbingService.requestDubbingV2(storyId, voiceId, memberId);
        } catch (StoryDubbingException e) {
            // 더빙 요청 중 오류 발생 시 500 에러 반환
            return ApiResponse.success(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        // 202로 우선 응답
        return ApiResponse.success(HttpStatus.ACCEPTED, null);
    }

    // 동화 더빙 완료 webhook
    @PostMapping("/{story-id}/members/{member-id}/voices/{voice-id}/dubbing/completed")
    public ApiResponse<?> completeVoiceDubbing(@PathVariable("story-id") Long storyId,
                                               @PathVariable("member-id") Long memberId,
                                               @PathVariable("voice-id") Long voiceId,
                                               @RequestParam("request-id") String requestId) {
        try {
            storyDubbingService.saveDubbingV2(storyId, memberId, voiceId, requestId);
        } catch (StoryDubbingException e) {
            // 더빙 저장 중 오류 발생 시 더빙 실패 알림 처리
            return ApiResponse.success(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        end = System.currentTimeMillis();
        log.info("dubbing ended: {}ms", end - start);
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
