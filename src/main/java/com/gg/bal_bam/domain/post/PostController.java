package com.gg.bal_bam.domain.post;

import com.gg.bal_bam.common.ResponseTemplate;
import com.gg.bal_bam.domain.post.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Post", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "게시글 생성 성공",
            useReturnTypeSchema = true
    )
    @PostMapping
    public ResponseTemplate<Void> createPost(
            @Validated @RequestBody PostRequest postRequest
            //, userid 필요
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");
        postService.createPost(postRequest, userId);
        return ResponseTemplate.ok();
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "게시글 수정 성공",
            useReturnTypeSchema = true
    )
    @PutMapping("/{postId}")
    public ResponseTemplate<Void> updatePost(
            @PathVariable Long postId,
            @Validated @RequestBody PostUpdateRequest postUpdateRequest
            //, userid 필요
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");
        postService.updatePost(postId, postUpdateRequest, userId);
        return ResponseTemplate.ok();
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "게시글 삭제 성공",
            useReturnTypeSchema = true
    )
    @DeleteMapping("/{postId}")
    public ResponseTemplate<Void> deletePost(
            @PathVariable Long postId
            //, userid 필요
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        postService.deletePost(postId, userId);
        return ResponseTemplate.ok();
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글을 상세 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "게시글 상세 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/{postId}")
    public ResponseTemplate<PostResponse> getPost(
            @PathVariable Long postId
    ) {
        return ResponseTemplate.ok(postService.getPostDetail(postId));
    }

    @Operation(summary = "피드 조회", description = "피드를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "피드 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping("/feed")
    public ResponseTemplate<Page<PostListResponse>> getFeed(
            @Validated @RequestBody PostListRequest postListRequest
            //, userid 필요
    ) {
        UUID userId = UUID.fromString("a8a73cde-cbc1-48dc-91cb-93c1ba8fe8c2");

        Pageable pageable = PageRequest.of(postListRequest.getOffset(), postListRequest.getLimit());
        return ResponseTemplate.ok(postService.getFeed(postListRequest, userId, pageable));
    }
}
