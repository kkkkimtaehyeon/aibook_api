package com.kth.aibook.controller.member;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.common.ApiResponseBody;
import com.kth.aibook.dto.member.*;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/api/members")
    public ApiResponse<Void> createMember(@Valid @RequestBody MemberCreateRequestDto createRequest) {
        memberService.createMember(createRequest);
        return ApiResponse.success(HttpStatus.CREATED);
    }


    @GetMapping("/api/members/me")
    public ResponseEntity<ApiResponseBody> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
        Long memberId = getMemberIdFromUserDetail(userDetail);
        MemberSimpleDto memberSimpleDto = memberService.getMemberSimpleInfoById(memberId);
        return ResponseEntity.ok(new ApiResponseBody(memberSimpleDto));
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

    private Long getMemberIdFromUserDetail(CustomUserDetails userDetail) {
        if (userDetail == null) {
            throw new MemberNotFoundException("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
        return Long.valueOf(userDetail.getUsername());
    }

}
