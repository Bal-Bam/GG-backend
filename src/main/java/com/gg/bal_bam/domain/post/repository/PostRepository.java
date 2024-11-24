package com.gg.bal_bam.domain.post.repository;

import com.gg.bal_bam.domain.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByParentPost(Post parentPost);

    @Query(value = "SELECT p FROM Post p " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= :distance")
    Page<Post> findNearbyPosts(@Param("latitude") double latitude,
                               @Param("longitude") double longitude,
                               @Param("distance") double distance,
                               Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    Page<Post> findPostsByUserIds(@Param("userIds") List<UUID> userIds, Pageable pageable);

}
