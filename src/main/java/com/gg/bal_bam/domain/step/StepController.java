package com.gg.bal_bam.domain.step;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.step.dto.DailyResponse;
import com.gg.bal_bam.domain.step.dto.MonthlyResponse;
import com.gg.bal_bam.domain.step.dto.WeeklyResponse;
import com.gg.bal_bam.domain.step.service.StepService;
import com.gg.bal_bam.domain.step.service.StepStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Step API", description = "산책을 시작, 종료하고 통계를 조회하는 API를 제공합니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/steps")
public class StepController {

    private final StepService stepService;
    private final StepStatsService stepStatsService;

    @Operation(summary = "산책 시작", description = "산책을 시작합니다")
    @ApiResponse(
            responseCode = "200",
            description = "산책 시작 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/start")
    public ResponseTemplate<Void> startWalk(
            //userId
    ) {

        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        stepService.startWalk(userId, LocalDateTime.now());
        return ResponseTemplate.ok();
    }


    @Operation(summary = "산책 종료", description = "산책을 종료합니다")
    @ApiResponse(
            responseCode = "200",
            description = "산책 종료 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/end")
    public ResponseTemplate<Void> endWalk(
            //userId
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        stepService.endWalk(userId, LocalDateTime.now());
        return ResponseTemplate.ok();
    }

    @Operation(summary = "일별 통계 조회", description = "특정 날짜의 산책 통계를 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "일별 통계 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/daily")
    public ResponseTemplate<DailyResponse> getDailyStats(
            //userId, date
    ) {

        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        return ResponseTemplate.ok(stepStatsService.getDailyStats(userId, LocalDate.now()));
    }

    @Operation(summary = "주간 통계 조회", description = "특정 기간의 주간 산책 통계를 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "주간 통계 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/weekly")
    public ResponseTemplate<WeeklyResponse> getWeeklyStats(
//            //userId
//            @RequestParam LocalDate startDate,
//            @RequestParam LocalDate endDate
    ) {

        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);

        return ResponseTemplate.ok(stepStatsService.getWeeklyStats(userId, start, end));
    }

    @Operation(summary = "월별 통계 조회", description = "특정 기간의 월별 산책 통계를 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "월별 통계 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/monthly")
    public ResponseTemplate<MonthlyResponse> getMonthlyStats(
            //userId
            @RequestParam @Parameter(description = "이번 달 기준일", example = "2024-11-01") LocalDate thisMonth
    ) {

        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        LocalDate lastMonth = thisMonth == null ? thisMonth : thisMonth.minusMonths(1);
        return ResponseTemplate.ok(stepStatsService.getMonthlyStats(userId, thisMonth, lastMonth));
    }
}
