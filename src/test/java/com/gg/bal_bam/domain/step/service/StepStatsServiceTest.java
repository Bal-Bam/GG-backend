package com.gg.bal_bam.domain.step.service;

import com.gg.bal_bam.domain.step.StepRepository;
import com.gg.bal_bam.domain.step.dto.DailyResponse;
import com.gg.bal_bam.domain.step.dto.MonthlyResponse;
import com.gg.bal_bam.domain.step.dto.WeeklyResponse;
import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StepStatsServiceTest {

    @InjectMocks
    private StepStatsService stepStatsService;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private UUID userId;

    private Step step;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.createUser("test@example.com", "password", "testuser");
        ReflectionTestUtils.setField(user, "id", userId);

        user.updateProfile("testuser", "testuser", "testuser", 10000, false);

        today = LocalDate.now();
    }

    @Test
    @DisplayName("일별 통계 조회 성공")
    void getDailyStatsSuccess() {
        // given
        step = Step.createStep(user, LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, today)).thenReturn(Optional.of(step));

        // when
        DailyResponse response = stepStatsService.getDailyStats(userId, today);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTotalWalkSteps()).isEqualTo(step.getTotalWalkSteps());
        assertThat(response.getTotalWalkTime()).isEqualTo(step.getTotalWalkTime());
        verify(userRepository, times(1)).findById(userId);
        verify(stepRepository, times(1)).findByUserAndDate(user, today);
    }

    @Test
    @DisplayName("일별 통계 조회 실패: Step이 존재하지 않음")
    void getDailyStatsStepNotFound() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, today)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> stepStatsService.getDailyStats(userId, today))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 날짜의 Step 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주간 통계 조회 성공")
    void geetWeeklyStatsSuccess() {
        // given
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        List<Step> steps = List.of(
                Step.createStep(user, LocalDate.now().minusDays(1)),
                Step.createStep(user, LocalDate.now().minusDays(2)),
                Step.createStep(user, LocalDate.now().minusDays(3))
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDateBetween(user, startDate, endDate)).thenReturn(steps);

        // when
        WeeklyResponse response = stepStatsService.getWeeklyStats(userId, startDate, endDate);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getWeeklyData()).hasSize(3);
        assertThat(response.getDailyGoalSteps()).isEqualTo(user.getDailyGoalSteps());
        verify(userRepository, times(1)).findById(userId);
        verify(stepRepository, times(1)).findByUserAndDateBetween(user, startDate, endDate);
    }

    @Test
    @DisplayName("월간 통계 조회 성공")
    void getMonthlyStatsSuccess() {
        //given
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(15);
        LocalDate lastMonth = thisMonth.minusMonths(1).withDayOfMonth(15);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.countGoalAchievedDays(eq(user), any(LocalDate.class), any(LocalDate.class))).thenReturn(10); //임의로 10일 목표 달성

        //when
        MonthlyResponse response = stepStatsService.getMonthlyStats(userId, thisMonth, lastMonth);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getThisMonthAchievementRate()).isGreaterThan(0f);
        assertThat(response.getLastMonthAchievementRate()).isGreaterThan(0f);
        verify(userRepository, times(1)).findById(userId);
        verify(stepRepository, times(2)).countGoalAchievedDays(eq(user), any(LocalDate.class), any(LocalDate.class));
    }
}