package com.gg.bal_bam.domain.post.repository;

import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    void deleteByPost(Post post);

    List<PostTag> findByPost(Post post);

}
