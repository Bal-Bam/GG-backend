package com.gg.bal_bam.domain.follow.repository;

import com.gg.bal_bam.domain.follow.model.Follow;
import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    @Query("select f.following.id from Follow f where f.follower.id = :followerId")
    List<UUID> findFollowedIdByFollowerId(UUID followerId);
}
