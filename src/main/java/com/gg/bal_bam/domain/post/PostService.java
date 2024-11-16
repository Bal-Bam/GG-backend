package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.domain.post.dto.PostRequest;
import com.gg.bal_bam.domain.post.dto.PostUpdateRequest;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public void createPost(PostRequest postRequest, UUID userId) {

        User user = null;// UserRepository 완료되면 추가

        Post parentPost = null;
        boolean isOriginal = true;

        // 답글 게시글인지 확인
        if (postRequest.getParentId() != null) {
            parentPost = postRepository.findById(postRequest.getParentId()).orElseThrow(
                    () -> new CustomException("부모 게시글이 존재하지 않습니다.")
            );

            if (!parentPost.getIsOriginal()) {
                throw new CustomException("부모 게시글은 메인 게시글이어야 합니다.");
            }

            isOriginal = false;

        }

        Post post = Post.createPost(
                user,
                parentPost,
                postRequest.getContent(),
                isOriginal,
                postRequest.getLatitude(),
                postRequest.getLongitude()
        );

        postRepository.save(post);

        //태그 사용자 처리 코드 추가
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, UUID userId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException("게시글이 존재하지 않습니다. 게시글 ID: " + postId)
        );

        // 작성자인지 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException("게시글 작성자만 수정할 수 있습니다.");
        }

        post.updatePost(postUpdateRequest.getContent());

        //태그 사용자 처리 코드 추가
    }
}
