package com.gg.bal_bam.domain.step.service;

import com.gg.bal_bam.domain.step.StepRepository;
import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepSchedulingServiceTest {

    @InjectMocks
    private StepSchedulingService stepSchedulingService;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.createUser("test1@example.com", "password", "testuser1");
        user2 = User.createUser("test2@example.com", "password", "testuser2");
    }

    @Test
    @DisplayName("오늘의 Step 데이터가 없으면 생성")
    void createStep() {
        // given
        LocalDate today = LocalDate.now();
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(stepRepository.findByUserAndDate(any(User.class), eq(today)))
                .thenReturn(Optional.empty());

        // when
        stepSchedulingService.createStep();

        // then
        verify(stepRepository, times(2)).save(any(Step.class));
    }

    @Test
    @DisplayName("오늘의 Step 데이터가 이미 존재하면 생성하지 않음")
    void skipCreateStep() {
        // given
        LocalDate today = LocalDate.now();
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(stepRepository.findByUserAndDate(user1, today)).thenReturn(Optional.of(mock(Step.class)));
        when(stepRepository.findByUserAndDate(user2, today)).thenReturn(Optional.empty());

        // when
        stepSchedulingService.createStep();

        // then
        verify(stepRepository, times(1)).save(any(Step.class));
    }

    @Test
    @DisplayName("오래된 Step 데이터 삭제")
    void cleanupOldSteps() {
        // given
        LocalDate cutoffDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);

        // when
        stepSchedulingService.deleteOldSteps();

        // then
        verify(stepRepository, times(1)).deleteStepsOlderThan(cutoffDate);
    }
}