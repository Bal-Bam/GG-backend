package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.domain.post.dto.PostRequest;
import com.gg.bal_bam.domain.post.dto.PostUpdateRequest;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.model.PostTag;
import com.gg.bal_bam.domain.post.repository.PostRepository;
import com.gg.bal_bam.domain.post.repository.PostTagRepository;
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
    private final PostTagRepository postTagRepository;

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

        //태그 사용자 처리 코드
        if (postRequest.getTaggedUsers() != null) {
            postRequest.getTaggedUsers().forEach(taggedUserRequest -> {
                User taggedUser = null; // UserRepository 완료되면 추가
                PostTag postTag = PostTag.createPostTag(post, taggedUser);
                postTagRepository.save(postTag);
            });
        }
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

        //태그 사용자 처리 코드: 기존 태그 사용자 삭제 후 추가
        postTagRepository.deleteByPost(post);
        if (postUpdateRequest.getTaggedUsers() != null) {
            postUpdateRequest.getTaggedUsers().forEach(taggedUserRequest -> {
                User taggedUser = null; // UserRepository 완료되면 추가
                PostTag postTag = PostTag.createPostTag(post, taggedUser);
                postTagRepository.save(postTag);
            });
        }
    }
}
