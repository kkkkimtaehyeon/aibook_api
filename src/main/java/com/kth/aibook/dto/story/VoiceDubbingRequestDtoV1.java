package com.kth.aibook.dto.story;

import com.kth.aibook.entity.member.Voice;
import com.kth.aibook.entity.story.Story;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class VoiceDubbingRequestDtoV1 {
    private String audioUrl;
    private Map<Long, String> storyPageMap = new HashMap<>();
    private String webhookUrl;

    public VoiceDubbingRequestDtoV1(Story story, Voice voice, String webhookUrl) {
        this.audioUrl = voice.getAudioUrl();
        story.getStoryPages().forEach(storyPage -> {
            storyPageMap.put(storyPage.getId(), storyPage.getContent());
        });
        this.webhookUrl = webhookUrl;
    }
}
