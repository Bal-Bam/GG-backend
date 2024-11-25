package com.gg.bal_bam.domain.step.service;

import com.gg.bal_bam.domain.step.StepRepository;
import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepServiceTest {

    @InjectMocks
    private StepService stepService;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private UUID userId;

    private Step step;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.createUser("test@example.com", "password", "testuser");
        ReflectionTestUtils.setField(user, "id", userId);

        user.updateProfile("testuser", "testuser", "testuser", 10000, false);

    }

    @Test
    @DisplayName("산책 시작 성공: Step 데이터가 있을 때")
    void startWalkStepIsExists() {
        // given
        LocalDateTime startTime = LocalDateTime.now();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.empty());

        // when
        stepService.startWalk(userId, startTime);

        // then
        verify(stepRepository, times(1)).save(any(Step.class));
    }

    @Test
    @DisplayName("산책 시작 성공: Step 데이터가 없을 때")
    void startWalkStepIsNotExists() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        step = Step.createStep(user, LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.of(step));

        // when
        stepService.startWalk(userId, startTime);

        // then
        verify(stepRepository, never()).save(any(Step.class));
    }

    @Test
    @DisplayName("산책 시작 실패: 이미 산책 중일 때")
    void startWalkAlreadyWalking() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        step = Step.createStep(user, LocalDate.now());

        step.startWalking(LocalDateTime.now().minusMinutes(30));  // 30분 전 산책 시작

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.of(step));

        // when / then
        assertThatThrownBy(() -> stepService.startWalk(userId, startTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 산책 중입니다.");
    }


    @Test
    @DisplayName("산책 종료 성공")
    void endWalkSuccess() {
        // given
        LocalDateTime endTime = LocalDateTime.now();
        step = Step.createStep(user, LocalDate.now());

        step.startWalking(LocalDateTime.now().minusMinutes(30));  // 30분 전 산책 시작

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.of(step));

        // when
        stepService.endWalk(userId, endTime);

        // then
        verify(stepRepository, never()).save(any(Step.class));

    }

    @Test
    @DisplayName("산책 종료 실패: Step 데이터가 없을 때")
    void endWalkStepNotFound() {
        // given
        LocalDateTime endTime = LocalDateTime.now();
        step = Step.createStep(user, LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> stepService.endWalk(userId, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage("오늘의 Step 데이터를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("산책 종료 실패: 산책 중이 아닐 때")
    void endWalkNotStarted() {
        // given
        LocalDateTime endTime = LocalDateTime.now();
        step = Step.createStep(user, LocalDate.now());
        ReflectionTestUtils.setField(step, "startTime", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.of(step));

        // when / then
        assertThatThrownBy(() -> stepService.endWalk(userId, endTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("산책 중이 아닙니다.");
    }

    @Test
    @DisplayName("산책")
    void calculateWalkTimeNotWalking() {
        //given
        LocalDateTime endTime = LocalDateTime.now();
        step = Step.createStep(user, LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(stepRepository.findByUserAndDate(user, LocalDate.now())).thenReturn(Optional.of(step));

        // when / then
        assertThatThrownBy(() -> stepService.endWalk(userId, endTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("산책 중이 아닙니다.");
    }

    @Test
    @DisplayName("목표 달성률 계산 성공")
    void calculateAchievementRateSuccess() {
        // given
        Integer totalWalkSteps = 10000; // 임의의 걸음 수
        Integer goal = 0; // 목표가 0인 경우

        // when
        Float result = ReflectionTestUtils.invokeMethod(stepService, "calculateGoalAchievementRate", totalWalkSteps, goal);

        // then
        assertThat(result).isEqualTo(0.0f);
    }

    @Test
    @DisplayName("목표 달성률 계산 성공: 정상적인 값")
    void calculateGoalAchievementRateNormalCase() {
        // given
        Integer totalWalkSteps = 5000;
        Integer goal = 10000;

        // when
        Float result = ReflectionTestUtils.invokeMethod(stepService, "calculateGoalAchievementRate", totalWalkSteps, goal);

        // then
        assertThat(result).isEqualTo(50.0f);
    }
}