package com.gg.bal_bam.domain.step;

import com.gg.bal_bam.domain.step.model.Step;
import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StepRepository extends JpaRepository<Step, Long> {

    Optional<Step> findByUserAndDate(User user, LocalDate date);

    List<Step> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(s) FROM Step s WHERE s.user = :user AND s.date BETWEEN :startDate AND :endDate AND s.goalAchievementRate >= 100")
    int countGoalAchievedDays(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
