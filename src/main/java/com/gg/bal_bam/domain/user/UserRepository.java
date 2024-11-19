package com.gg.bal_bam.domain.user;

import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);

    // 팔로우 추천 -> 랜덤 유저 목록 조회
    @Query("select u from User u where u.id != :userId and u.id not in :followedUserIds order by function('RAND')")
    List<User> findRandomUsers(@Param("userId") UUID userId, @Param("followedUserIds") List<UUID> followedUserIds, Pageable pageable);
}
