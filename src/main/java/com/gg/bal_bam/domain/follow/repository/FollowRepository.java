package com.gg.bal_bam.domain.follow.repository;

import com.gg.bal_bam.domain.follow.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
