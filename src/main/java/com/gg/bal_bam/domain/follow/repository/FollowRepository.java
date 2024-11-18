package com.gg.bal_bam.domain.follow.repository;

import com.gg.bal_bam.domain.follow.model.Follow;
import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
