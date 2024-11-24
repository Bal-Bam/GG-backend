package com.gg.bal_bam.domain.step.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WalkRequest {

    @NotNull(message = "시작 시간은 필수 값입니다.")
    private LocalDateTime startTime;
}
