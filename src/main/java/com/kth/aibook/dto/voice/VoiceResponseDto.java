package com.kth.aibook.dto.voice;


import com.kth.aibook.dto.member.VoiceDto;

import java.util.List;

public record VoiceResponseDto(List<VoiceDto> myVoices, List<VoiceDto> defaultVoices) {
}
