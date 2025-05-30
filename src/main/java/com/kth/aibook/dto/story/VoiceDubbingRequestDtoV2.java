package com.kth.aibook.dto.story;

import lombok.Data;

import java.util.Map;

@Data
public class VoiceDubbingRequestDtoV2 {
    private String voiceAudioUrl;
    private Map<Long, DubbingContentAndPreSignedUrlDto> storyPageMap;
    private String webhookUrl;

    public VoiceDubbingRequestDtoV2(String voiceAudioUrl, Map<Long, DubbingContentAndPreSignedUrlDto> storyPageMap, String webhookUrl) {
        this.voiceAudioUrl = voiceAudioUrl;
        this.storyPageMap = storyPageMap;
        this.webhookUrl = webhookUrl;
    }
}
