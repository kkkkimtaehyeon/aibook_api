package com.kth.aibook.controller.voice;

import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.member.VoiceDto;
import com.kth.aibook.dto.member.VoiceUploadRequestDto;
import com.kth.aibook.dto.voice.VoiceResponseDto;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.voice.VoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VoiceController {
    private final VoiceService voiceService;

    // 목소리 등록
    @PostMapping("/api/voices")
    public ResponseEntity<ApiResponseBody> uploadVoice(@AuthenticationPrincipal CustomUserDetails userDetail,
                                                       @Valid @ModelAttribute VoiceUploadRequestDto voiceUploadRequest) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        voiceService.uploadMyVoice(memberId, voiceUploadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 목소리 목록 조회
    @GetMapping("/api/voices")
    public ResponseEntity<ApiResponseBody> getVoices(@AuthenticationPrincipal CustomUserDetails userDetail) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        List<VoiceDto> myVoices = voiceService.getVoices(memberId);

        return ResponseEntity.ok(new ApiResponseBody(new VoiceResponseDto(myVoices, null)));
    }

    // 목소리 삭제
    @DeleteMapping("/api/voices/{voice-id}")
    public ResponseEntity<Void> deleteMemberVoices(@AuthenticationPrincipal CustomUserDetails userDetail,
                                                          @PathVariable("voice-id") Long voiceId) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        voiceService.deleteVoice(memberId, voiceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Long getMemberIdFromUserDetail(CustomUserDetails userDetail) {
        if (userDetail == null) {
            throw new MemberNotFoundException("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
        return Long.valueOf(userDetail.getUsername());
    }
}
