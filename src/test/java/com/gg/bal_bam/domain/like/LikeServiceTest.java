package com.gg.bal_bam.domain.like;

import com.gg.bal_bam.domain.like.dto.LikeResponse;
import com.gg.bal_bam.domain.like.model.Like;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.repository.PostRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.model.User;
import com.gg.bal_bam.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private Post post;
    private User user;
    private Long postId;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        postId = 1L;
        userId = UUID.randomUUID();
        user = User.createUser("test@example.com", "password", "testuser");
        post = Post.createPost(user, null, "Sample content", true, 37.7749, -122.4194);
    }

    @Test
    @DisplayName("좋아요 성공")
    void like() {

        //given
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
        when(likeRepository.countByPostAndIsLikedTrue(post)).thenReturn(1L);

        //when
        LikeResponse response = likeService.like(postId, userId);

        //then
        verify(likeRepository).save(any(Like.class));
        assertThat(response.getPostId()).isEqualTo(postId);
        assertThat(response.isLiked()).isTrue();
        assertThat(response.getLikeCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void toggleLike() {

        //given
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Like existLike = Like.createLike(user, post);
        when(likeRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(existLike));
        when(likeRepository.countByPostAndIsLikedTrue(post)).thenReturn(0L);

        //when
        LikeResponse response = likeService.like(postId, userId);

        //then
        verify(likeRepository, never()).save(any(Like.class));
        assertThat(response.getPostId()).isEqualTo(postId);
        assertThat(response.isLiked()).isFalse();
        assertThat(response.getLikeCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("좋아요 실패 - 유저가 존재하지 않음")
    void likeUserNotFound() {

        //given
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> likeService.like(postId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 유저가 존재하지 않습니다. UserId: " + userId);
    }

    @Test
    @DisplayName("좋아요 실패 - 포스트가 존재하지 않음")
    void likePostNotFound() {

        //given
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        //when / then
        assertThatThrownBy(() -> likeService.like(postId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다. PostId: " + postId);
    }
}