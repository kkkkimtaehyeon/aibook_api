package com.kth.aibook.controller.story;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.common.exception.StoryDubbingException;
import com.kth.aibook.dto.story.VoiceDubbingResponseDto;
import com.kth.aibook.service.story.StoryDubbingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryDubbingController {
    private final StoryDubbingService storyDubbingService;

    // 동화 더빙 업로드
    @PostMapping("/{story-id}/dubbing")
    public ApiResponse<Void> addDubbing(@PathVariable("story-id") Long storyId,
                                        @RequestParam("files") List<MultipartFile> files) {
        storyDubbingService.addCustomDubbing(storyId, files);
        return ApiResponse.success(HttpStatus.OK, null);
    }

    // 보이스 클로닝 더빙 요청
    @PostMapping("/{story-id}/voices/{voice-id}/dubbing")
    public ApiResponse<?> requestVoiceDubbing(@PathVariable("story-id") Long storyId,
                                              @PathVariable("voice-id") Long voiceId,
                                              @AuthenticationPrincipal CustomUserDetails userDetail) {
        Long memberId = userDetail.getMemberId();
        try {
            storyDubbingService.requestDubbing(storyId, voiceId, memberId);
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
                                               @RequestBody VoiceDubbingResponseDto dubbingResponse) {
        try {
            storyDubbingService.saveDubbing(storyId, memberId, voiceId, dubbingResponse);
        } catch (StoryDubbingException e) {
            // 더빙 저장 중 오류 발생 시 더빙 실패 알림 처리
            return ApiResponse.success(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
