package com.kth.aibook.controller.member;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.CustomUserDetails;
import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.dto.member.MemberDetailDto;
import com.kth.aibook.dto.member.MemberSimpleDto;
import com.kth.aibook.exception.member.MemberNotFoundException;
import com.kth.aibook.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        if (userDetail == null) {
            throw new MemberNotFoundException("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
        Long memberId = Long.valueOf(userDetail.getUsername());
        MemberSimpleDto memberSimpleDto = memberService.getMemberSimpleInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberSimpleDto);
    }

    // 회원정보
    @GetMapping("/api/me/detail")
    public ApiResponse<MemberDetailDto> getMyDetailInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
        if (userDetail == null) {
            throw new MemberNotFoundException("회원 정보를 가져오는 중 오류가 발생했습니다.");
        }
        Long memberId = Long.valueOf(userDetail.getUsername());
        MemberDetailDto memberDetail = memberService.getMemberDetailInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberDetail);
    }

    @GetMapping("/api/members/{member-id}")
    public ApiResponse<MemberDetailDto> getMember(@PathVariable("member-id") Long memberId) {
        MemberDetailDto memberDetail = memberService.getMemberDetailInfoById(memberId);
        return ApiResponse.success(HttpStatus.OK, memberDetail);
    }

}
