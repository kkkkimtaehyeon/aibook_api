package com.kth.aibook.dto.member;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.MemberRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MemberCreateRequestDto {
    @NotBlank(message = "이메일은 필수값입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수값입니다.")
    @Length(min = 2, max = 12, message = "닉네임은 2~12 자리 사이입니다.")
    private String nickName;

    @NotNull(message = "생년월일은 필수값입니다.")
    private LocalDate birthDate;

    @Nullable
    private String oauthProvider;

    @Nullable
    private Long oauthProviderMemberId;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickName(nickName)
                .birthDate(birthDate)
                .createdAt(LocalDateTime.now())
                .role(MemberRole.MEMBER)
                .build();
    }
}
