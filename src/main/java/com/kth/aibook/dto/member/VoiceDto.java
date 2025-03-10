package com.kth.aibook.dto.member;

import com.kth.aibook.entity.member.Voice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VoiceDto {
    private String name;

    public VoiceDto(Voice voice) {
        this.name = voice.getName();
    }
}
