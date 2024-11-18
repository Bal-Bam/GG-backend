package com.gg.bal_bam.domain.follow.repository;

import com.gg.bal_bam.domain.follow.model.PendingFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRequestRepository extends JpaRepository<PendingFollow, Long> {
}
