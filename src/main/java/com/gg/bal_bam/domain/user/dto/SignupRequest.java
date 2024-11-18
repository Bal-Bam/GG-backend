package com.gg.bal_bam.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignupRequest {
    @Schema(description = "신규가입 이메일")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;

    @Schema(description = "신규가입 유저네임")
    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
    private String username;

    @Schema(description = "신규가입 비밀번호")
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;


}