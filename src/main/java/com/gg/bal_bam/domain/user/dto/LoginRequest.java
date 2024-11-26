package com.gg.bal_bam.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "로그인 시도 계졍 이메일 또는 유저네임")
    @NotBlank(message = "이메일 또는 사용자 이름은 필수 입력 항목입니다.")
    private String identifier; // 이메일 또는 유저네임

    @Schema(description = "로그인 시도 계정 비밀번호")
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;
}