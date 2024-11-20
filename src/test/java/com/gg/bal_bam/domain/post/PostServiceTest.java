package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.domain.follow.repository.FollowRepository;
import com.gg.bal_bam.domain.post.dto.*;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.repository.PostRepository;
import com.gg.bal_bam.domain.post.repository.PostTagRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    private UUID userId;
    private PostRequest postRequest;
    private PostUpdateRequest postUpdateRequest;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.createUser("test@example.com", "password", "testuser");
        ReflectionTestUtils.setField(user, "id", userId);

        postRequest = new PostRequest("게시글 내용", 37.7749, 127.1234, null, new ArrayList<>());
        postUpdateRequest = new PostUpdateRequest("수정된 게시글 내용", new ArrayList<>());
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPostSuccess() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(Post.createPost(
                user, null, postRequest.getContent(), true, postRequest.getLatitude(), postRequest.getLongitude())
        );

        // when
        Throwable thrown = catchThrowable(() -> postService.createPost(postRequest, userId));

        // then
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, times(1)).save(any());
        assertThat(thrown).isNull();
    }

    @Test
    @DisplayName("게시글 작성 실패 - 사용자가 존재하지 않음")
    void createPostFailUserNotFound() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> postService.createPost(postRequest, userId));

        // then
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, never()).save(any());
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("사용자가 존재하지 않습니다. UserId: " + userId);
    }


    @Test
    @DisplayName("게시글 작성 실패 - 부모 게시글이 존재하지 않음")
    void createPostFailParentPostNotFound() {
        // given
        ReflectionTestUtils.setField(postRequest, "parentId", 1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postRequest.getParentId())).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> postService.createPost(postRequest, userId));

        // then
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, never()).save(any());
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("부모 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePostSuccess() {
        // given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        postService.updatePost(postId, postUpdateRequest, userId);

        // then
        verify(postRepository, times(1)).findById(postId);
        assertThat(post.getContent()).isEqualTo(postUpdateRequest.getContent());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 작성자가 아님")
    void updatePostFailNotAuthor() {
        // given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);

        UUID otherUserId = UUID.randomUUID();
        User otherUser = User.createUser("other@example.com", "password", "otheruser");
        ReflectionTestUtils.setField(otherUser, "id", otherUserId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        Throwable thrown = catchThrowable(() -> postService.updatePost(postId, postUpdateRequest, otherUserId));

        // then
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("게시글 작성자만 수정할 수 있습니다.");

    }

    @Test
    @DisplayName("게시글 수정 실패 - 게시글이 존재하지 않음")
    void updatePostFailPostNotFound() {
        // given
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> postService.updatePost(postId, postUpdateRequest, userId));

        // then
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("게시글이 존재하지 않습니다. 게시글 ID: " + postId);
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deletePostSuccess() {
        // given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        Throwable thrown = catchThrowable(() -> postService.deletePost(postId, userId));

        // then
        assertThat(thrown).isNull();
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 작성자가 아님")
    void deletePostFailNotAuthor() {
        // given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        UUID otherUserId = UUID.randomUUID();

        // when
        Throwable thrown = catchThrowable(() -> postService.deletePost(postId, otherUserId));

        // then
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("게시글 작성자만 삭제할 수 있습니다. 요청자 ID: " + otherUserId + ", 작성자 ID: " + userId);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글이 존재하지 않음")
    void deletePostFailPostNotFound() {
        // given
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> postService.deletePost(postId, userId));

        // then
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("게시글이 존재하지 않습니다. 게시글 ID: " + postId);
    }

    @Test
    @DisplayName("게시글 상세 조회 성공")
    void getPostDetailSuccess() {
        // given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);
        ReflectionTestUtils.setField(post, "id", postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postTagRepository.findByPost(post)).thenReturn(new ArrayList<>());
        when(postRepository.findByParentPost(post)).thenReturn(new ArrayList<>());

        // when
        PostResponse response = postService.getPostDetail(postId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPostId()).isEqualTo(postId);
        assertThat(response.getContent()).isEqualTo(post.getContent());
        verify(postRepository, times(1)).findById(postId);
        verify(postTagRepository, times(1)).findByPost(post);
        verify(postRepository, times(1)).findByParentPost(post);
    }

    @Test
    @DisplayName("게시글 상세 조회 실패 - 게시글이 존재하지 않음")
    void getPostDetailFailPostNotFound() {
        // given
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> postService.getPostDetail(postId));

        // then
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("게시글이 존재하지 않습니다. 게시글 ID: " + postId);
    }

    @Test
    @DisplayName("피드 조회 성공")
    void getFeedSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        List<UUID> followedUserIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        PostListRequest postListRequest = new PostListRequest(0, 10, 37.7749, 127.1234);
        Pageable pageable = PageRequest.of(postListRequest.getOffset(), postListRequest.getLimit());

        // 팔로우한 사용자의 게시글
        Post post1 = Post.createPost(user, null, "팔로우한 사용자의 게시글", true, 37.7749, 127.1234);
        List<Post> followedUserPosts = List.of(post1);
        ReflectionTestUtils.setField(post1, "createdAt", LocalDateTime.now());

        // 근처 사용자의 게시글
        Post post2 = Post.createPost(user, null, "근처 사용자의 게시글", true, 37.7749, 127.1234);
        List<Post> nearbyPosts = List.of(post2);
        ReflectionTestUtils.setField(post2, "createdAt", LocalDateTime.now());

        when(followRepository.findFollowedIdByFollowerId(userId)).thenReturn(followedUserIds);
        when(postRepository.findPostsByUserIds(followedUserIds, pageable)).thenReturn(followedUserPosts);
        when(postRepository.findNearbyPosts(postListRequest.getLatitude(), postListRequest.getLongitude(), 3000.0, pageable)).thenReturn(nearbyPosts);

        // when
        List<PostListResponse> feed = postService.getFeed(postListRequest, userId);

        // then
        assertThat(feed).isNotNull();
        assertThat(feed).hasSize(2);
        verify(followRepository, times(1)).findFollowedIdByFollowerId(userId);
        verify(postRepository, times(1)).findPostsByUserIds(followedUserIds, pageable);
        verify(postRepository, times(1)).findNearbyPosts(postListRequest.getLatitude(), postListRequest.getLongitude(), 3000.0, pageable);
    }
}