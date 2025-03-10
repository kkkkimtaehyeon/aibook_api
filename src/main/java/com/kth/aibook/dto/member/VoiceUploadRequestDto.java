package com.kth.aibook.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VoiceUploadRequestDto {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private MultipartFile audioFile;
}
