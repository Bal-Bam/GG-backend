package com.gg.bal_bam.domain.post.repository;

import com.gg.bal_bam.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByParentPost(Post parentPost);

    @Query(value = "SELECT * FROM posts " +
            "WHERE ST_Distance_Sphere(point(longitude, latitude), point(:longitude, :latitude)) <= :distance " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Post> findNearbyPosts(@Param("latitude") double latitude,
                               @Param("longitude") double longitude,
                               @Param("distance") double distance,
                               @Param("limit") int limit,
                               @Param("offset") int offset);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    List<Post> findPostsByUserIds(@Param("userIds") List<UUID> userIds, Pageable pageable);

}
