package com.gg.bal_bam.domain.user;

import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // 회원가입 용
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    // 로그인 등
    Optional<User> findByEmail(String email);

    // 팔로우 추천 -> 랜덤 유저 목록 조회
    @Query("select u from User u where u.id != :userId and u.id not in :followedUserIds order by function('RAND')")
    Page<User> findRandomUsers(@Param("userId") UUID userId, @Param("followedUserIds") List<UUID> followedUserIds, Pageable pageable);
}
