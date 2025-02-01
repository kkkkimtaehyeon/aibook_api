package com.kth.aibook.controller.story;

import com.kth.aibook.common.ApiResponse;
import com.kth.aibook.common.provider.JwtProvider;
import com.kth.aibook.dto.story.BaseStoryCreateRequestDto;
import com.kth.aibook.service.story.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/stories")
@RestController
public class StoryController {
    private final StoryService storyService;
    private final JwtProvider jwtProvider;

    @PostMapping("/base-story")
    public ApiResponse<?> createBaseStory(@RequestHeader("Authorization") String token,
                                          @RequestBody BaseStoryCreateRequestDto createRequest) {
        long memberId = getMemberIdFromToken(token);
        storyService.createBaseStory(memberId, createRequest);

        return ApiResponse.success(HttpStatus.CREATED, createRequest);
    }

    private long getMemberIdFromToken(String token) {
        jwtProvider.validateToken(token);
        return jwtProvider.getMemberIdFromToken(token);
    }
}
