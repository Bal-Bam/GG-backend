package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.domain.follow.repository.FollowRepository;
import com.gg.bal_bam.domain.post.dto.PostRequest;
import com.gg.bal_bam.domain.post.dto.PostUpdateRequest;
import com.gg.bal_bam.domain.post.model.Post;
import com.gg.bal_bam.domain.post.model.PostTag;
import com.gg.bal_bam.domain.post.repository.PostRepository;
import com.gg.bal_bam.domain.post.repository.PostTagRepository;
import com.gg.bal_bam.domain.user.UserRepository;
import com.gg.bal_bam.domain.user.dto.TaggedUserRequest;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostTagTest {

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
    private User user;

    private UUID taggedUserId;
    private User taggedUser;

    private PostRequest postRequest;
    private PostUpdateRequest postUpdateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.createUser("test@example.com", "password", "testuser");
        ReflectionTestUtils.setField(user, "id", userId);

        taggedUserId = UUID.randomUUID();
        taggedUser = User.createUser("tagUser@example.com", "password", "taguser");
        ReflectionTestUtils.setField(taggedUser, "id", taggedUserId);

        postRequest = new PostRequest("게시글 내용", 37.7749, 127.1234, null, new ArrayList<>());
        postUpdateRequest = new PostUpdateRequest("수정된 게시글 내용", new ArrayList<>());
    }

    @Test
    @DisplayName("게시글 작성 시 태그된 사용자 처리 - 태그된 사용자 존재")
    void createPostWithTaggedUsersSuccess() {
        //given
        postRequest.getTaggedUsers().add(new TaggedUserRequest(taggedUserId, "taggedUser"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findAllById(List.of(taggedUserId))).thenReturn(List.of(taggedUser));

        when(postRepository.save(any(Post.class))).thenReturn(Post.createPost(
                user,
                null,
                postRequest.getContent(),
                true,
                postRequest.getLatitude(),
                postRequest.getLongitude()
        ));


        //when
        Throwable thrown = catchThrowable(() -> postService.createPost(postRequest, userId));

        //then
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postTagRepository, times(1)).saveAll(anyList());
        assertThat(thrown).isNull();
    }

    @Test
    @DisplayName("게시글 작성 시 태그된 사용자 처리 - 태그된 사용자 미존재")
    void createPostWithTaggedUsersFail() {
        //given
        postRequest.getTaggedUsers().add(new TaggedUserRequest(taggedUserId, "taggedUser"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findAllById(List.of(taggedUserId))).thenReturn(List.of());

        //when
        Throwable thrown = catchThrowable(() -> postService.createPost(postRequest, userId));

        //then
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postTagRepository, never()).saveAll(anyList());
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("태그된 사용자 중 일부 사용자가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시글 수정 시 태그된 사용자 처리 - 태그된 사용자 존재")
    void updatePostWithTaggedUsersSuccess() {
        //given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);

        postUpdateRequest.getTaggedUsers().add(new TaggedUserRequest(taggedUserId, "taggedUser"));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findAllById(List.of(taggedUserId))).thenReturn(List.of(taggedUser));

        //when
        Throwable thrown = catchThrowable(() -> postService.updatePost(1L, postUpdateRequest, userId));

        //then
        verify(postTagRepository, times(1)).deleteByPost(post);
        verify(postTagRepository, times(1)).saveAll(anyList());
        assertThat(thrown).isNull();
    }

    @Test
    @DisplayName("게시글 수정 시 태그된 사용자 처리 - 태그된 사용자 미존재")
    void updatePostWithTaggedUsersFail() {
        //given
        Long postId = 1L;
        Post post = Post.createPost(user, null, "게시글 내용", true, 37.7749, 127.1234);

        postUpdateRequest.getTaggedUsers().add(new TaggedUserRequest(taggedUserId, "taggedUser"));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findAllById(List.of(taggedUserId))).thenReturn(List.of());

        //when
        Throwable thrown = catchThrowable(() -> postService.updatePost(1L, postUpdateRequest, userId));

        //then
        verify(postTagRepository, times(1)).deleteByPost(post);
        verify(postTagRepository, never()).saveAll(anyList());
        assertThat(thrown).isInstanceOf(CustomException.class).hasMessage("태그된 사용자 중 일부 사용자가 존재하지 않습니다.");
    }
}
