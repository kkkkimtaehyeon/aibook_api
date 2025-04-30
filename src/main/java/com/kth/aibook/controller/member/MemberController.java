package com.kth.aibook.controller.member;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.member.*;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.cloud.CloudStorageService;
import com.kth.aibook.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/members")
    public ApiResponse<Void> createMember(@Valid @RequestBody MemberCreateRequestDto createRequest) {
        memberService.createMember(createRequest);
        return ApiResponse.success(HttpStatus.CREATED);
    }

    // 회원정보
    @GetMapping("/api/me")
    public ApiResponse<MemberSimpleDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        MemberSimpleDto memberSimpleDto = memberService.getMemberSimpleInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberSimpleDto);
    }

    // 회원정보
    @GetMapping("/api/me/detail")
    public ApiResponse<MemberDetailDto> getMyDetailInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        MemberDetailDto memberDetail = memberService.getMemberDetailInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberDetail);
    }

    // 회원 상세정보 조회
    @GetMapping("/api/members/{member-id}")
    public ApiResponse<MemberDetailDto> getMember(@PathVariable("member-id") Long memberId) {
        MemberDetailDto memberDetail = memberService.getMemberDetailInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberDetail);
    }

    // 목소리 등록
    @PostMapping("/api/voices")
    public ApiResponse<Void> uploadVoice(@AuthenticationPrincipal CustomUserDetails userDetail,
                                         @Valid @ModelAttribute VoiceUploadRequestDto voiceUploadRequest) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        memberService.uploadVoice(memberId, voiceUploadRequest);
        return ApiResponse.success(HttpStatus.OK, null);
    }

    // 목소리 목록 조회
    @GetMapping("/api/voices")
    public ApiResponse<List<VoiceDto>> getMemberVoices(@AuthenticationPrincipal CustomUserDetails userDetail) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        List<VoiceDto> voices = memberService.getVoices(memberId);
        return ApiResponse.success(HttpStatus.OK, voices);
    }

    /**
     * 목소리 삭제
     *
     * @param userDetail
     * @param voiceId
     * @return
     */
    @DeleteMapping("/api/voices/{voice-id}")
    public ApiResponse<List<VoiceDto>> deleteMemberVoices(@AuthenticationPrincipal CustomUserDetails userDetail,
                                                          @PathVariable("voice-id") Long voiceId) {
        memberService.removeVoices(voiceId);
        return ApiResponse.success(HttpStatus.NO_CONTENT);
    }


    private Long getMemberIdFromUserDetail(CustomUserDetails userDetail) {
        if (userDetail == null) {
            throw new MemberNotFoundException("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
        return Long.valueOf(userDetail.getUsername());
    }

}
