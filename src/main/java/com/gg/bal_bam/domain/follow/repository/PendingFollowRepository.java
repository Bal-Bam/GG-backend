package com.gg.bal_bam.domain.follow.repository;

import com.gg.bal_bam.domain.follow.model.PendingFollow;
import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PendingFollowRepository extends JpaRepository<PendingFollow, Long> {

    boolean existsByRequesterAndTarget(User requester, User target);

    Optional<PendingFollow> findByRequesterAndTarget(UUID requesterId, UUID targetId);
}
