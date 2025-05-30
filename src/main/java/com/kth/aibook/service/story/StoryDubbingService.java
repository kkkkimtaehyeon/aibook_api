package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.VoiceDubbingResponseDto;

public interface StoryDubbingService {

//    void addCustomDubbing(Long storyId, List<MultipartFile> files);

    void requestDubbingV1(Long storyId, Long voiceId, Long memberId);
    void requestDubbingV2(Long storyId, Long voiceId, Long memberId);
    void requestDubbingV3(Long storyId, Long voiceId, Long memberId);

    void saveDubbingV1(Long storyId, Long memberId, Long voiceId, VoiceDubbingResponseDto response);
    void saveDubbingV2(Long storyId, Long memberId, Long voiceId, String requestId);

}
