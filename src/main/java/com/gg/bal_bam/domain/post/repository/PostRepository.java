package com.gg.bal_bam.domain.post.repository;

import com.gg.bal_bam.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByParentPost(Post parentPost);

}
