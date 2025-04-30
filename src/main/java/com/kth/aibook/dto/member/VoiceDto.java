package com.kth.aibook.dto.member;

import com.kth.aibook.entity.member.Voice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VoiceDto {
    private long id;
    private String name;

    public VoiceDto(Voice voice) {
        this.id = voice.getId();
        this.name = voice.getName();
    }

    public VoiceDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
