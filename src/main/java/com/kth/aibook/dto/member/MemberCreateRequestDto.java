package com.kth.aibook.dto.member;

import com.kth.aibook.entity.member.Member;
import com.kth.aibook.entity.member.MemberRole;
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
    @Email(message = "이메일은 필수값입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수값입니다.")
    @Length(min = 5, max = 15)
    private String nickName;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    private String oauthProvider;

    @NotNull
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
