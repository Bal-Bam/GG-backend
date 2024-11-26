package com.gg.bal_bam.domain.health;

import com.gg.bal_bam.common.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HealthTip API", description = "AI를 활용한 건강 팁 API를 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/health")
public class HealthTipsController {

    private final HealthTipsService healthTipsService;

    @Operation(summary = "건강 팁 제공", description = "AI를 활용하여 오늘의 건강 팁을 제공합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "건강 팁 제공 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/tips")
    public ResponseTemplate<String> getHealthTip() {
        return ResponseTemplate.ok(healthTipsService.generateHealthTip());
    }
}
