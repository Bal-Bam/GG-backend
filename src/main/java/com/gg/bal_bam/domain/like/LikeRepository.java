package com.gg.bal_bam.domain.like;

import com.gg.bal_bam.domain.like.model.Like;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);

    long countByPostAndIsLikedTrue(Post post);
}
