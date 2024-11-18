package com.gg.bal_bam.domain.like;

import com.gg.bal_bam.domain.like.dto.LikeResponse;
import com.gg.bal_bam.domain.like.model.Like;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.repository.PostRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse like(Long postId, UUID userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("해당 게시글이 존재하지 않습니다. PostId: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 유저가 존재하지 않습니다. UserId: " + userId));


        Like like = likeRepository.findByUserAndPost(user, post).orElse(null);

        if (like == null) {
            like = Like.createLike(user, post);
            likeRepository.save(like);
        } else {
            like.updateLike();
        }

        long totalLikeCount = likeRepository.countByPostAndIsLikedTrue(post);

        return LikeResponse.of(postId, like.isLiked(), totalLikeCount);
    }

}
