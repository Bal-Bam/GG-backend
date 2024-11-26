package com.gg.bal_bam.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TaggedUserRequest {

    @Schema(description = "태그된 사용자의 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "userId는 필수 값입니다.")
    private UUID userId;

    @Schema(description = "태그된 사용자의 이름")
    @NotNull(message = "username은 필수 값입니다.")
    private String username;
}
