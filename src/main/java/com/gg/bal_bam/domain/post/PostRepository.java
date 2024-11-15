package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
