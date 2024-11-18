package com.gg.bal_bam.domain.follow.repository;

import com.gg.bal_bam.domain.follow.model.Follow;
import com.gg.bal_bam.domain.follow.model.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
}
