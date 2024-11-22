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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        stepService.startWalk(null, LocalDateTime.now());
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
        stepService.endWalk(null, LocalDateTime.now());
        return ResponseTemplate.ok();
    }

    @Operation(summary = "일별 통계 조회", description = "특정 날짜의 산책 통계를 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "일별 통계 조회 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/daily")
    public ResponseTemplate<DailyResponse> getDailyStats(
            //userId, date
    ) {
        return ResponseTemplate.ok(stepStatsService.getDailyStats(null, LocalDate.now()));
    }

    @Operation(summary = "주간 통계 조회", description = "특정 기간의 주간 산책 통계를 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "주간 통계 조회 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/weekly")
    public ResponseTemplate<WeeklyResponse> getWeeklyStats(
            //userId
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        LocalDate start = startDate == null ? startDate : LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate end = endDate == null ? endDate : start.plusDays(6);

        return ResponseTemplate.ok(stepStatsService.getWeeklyStats(null, start, end));
    }

    @Operation(summary = "월별 통계 조회", description = "특정 기간의 월별 산책 통계를 조회합니다")
    @ApiResponse(
            responseCode = "200",
            description = "월별 통계 조회 성공",
            useReturnTypeSchema = true
    )
    @PostMapping("/monthly")
    public ResponseTemplate<MonthlyResponse> getMonthlyStats(
            //userId
            @RequestParam @Parameter(description = "이번 달 기준일", example = "2024-11-01") LocalDate thisMonth
    ) {
        LocalDate lastMonth = thisMonth == null ? thisMonth : thisMonth.minusMonths(1);
        return ResponseTemplate.ok(stepStatsService.getMonthlyStats(null, thisMonth, lastMonth));
    }
}
