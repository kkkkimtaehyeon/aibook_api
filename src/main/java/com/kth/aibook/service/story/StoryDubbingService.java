package com.kth.aibook.service.story;

import com.kth.aibook.dto.story.VoiceDubbingResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryDubbingService {

    void addCustomDubbing(Long storyId, List<MultipartFile> files);

    void requestDubbing(Long storyId, Long voiceId, Long memberId);

    void saveDubbing(Long storyId, Long memberId, Long voiceId, VoiceDubbingResponseDto response);

}
