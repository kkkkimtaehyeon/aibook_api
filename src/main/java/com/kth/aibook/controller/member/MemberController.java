package com.kth.aibook.controller.member;

import com.kth.aibook.dto.member.MemberCreateRequestDto;
import com.kth.aibook.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public void createMember(@Valid @RequestBody MemberCreateRequestDto createRequest) {
        memberService.createMember(createRequest);
    }
}
