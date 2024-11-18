package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.domain.post.dto.PostRequest;
import com.gg.bal_bam.domain.post.dto.PostResponse;
import com.gg.bal_bam.domain.post.dto.PostUpdateRequest;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.model.PostTag;
import com.gg.bal_bam.domain.post.repository.PostRepository;
import com.gg.bal_bam.domain.post.repository.PostTagRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.dto.TaggedUserRequest;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 작성
     */
    @Transactional
    public void createPost(PostRequest postRequest, UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException("사용자가 존재하지 않습니다. UserId: " + userId)
        );

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
        processTaggedUsers(post, postRequest.getTaggedUsers());
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
        processTaggedUsers(post, postUpdateRequest.getTaggedUsers());
    }

    private void processTaggedUsers(Post post, List<TaggedUserRequest> taggedUsers) {
        if (taggedUsers != null) {
            taggedUsers.forEach(taggedUserRequest -> {
                User taggedUser = userRepository.findById(taggedUserRequest.getUserId()).orElseThrow(
                        () -> new CustomException("태그된 사용자가 존재하지 않습니다. 사용자 ID: " + taggedUserRequest.getUserId())
                );

                PostTag postTag = PostTag.createPostTag(post, taggedUser);
                postTagRepository.save(postTag);
            });
        }
    }

    @Transactional
    public void deletePost(Long postId, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException("게시글이 존재하지 않습니다. 게시글 ID: " + postId)
        );

        // 작성자인지 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException("게시글 작성자만 삭제할 수 있습니다. 작성자 ID: " + userId);
        }

        //관련 태그 삭제
        postTagRepository.deleteByPost(post);

        postRepository.delete(post);
    }

    // 게시글 상세 조회
    @Transactional
    public PostResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException("게시글이 존재하지 않습니다. 게시글 ID: " + postId)
        );

        List<PostTag> postTags = postTagRepository.findByPost(post);

        List<Post> childPosts = postRepository.findByParentPost(post);

        return PostResponse.of(post, postTags, childPosts);

    }
}
