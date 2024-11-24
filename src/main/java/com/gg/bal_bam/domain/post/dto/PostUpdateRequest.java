package com.gg.bal_bam.domain.post.dto;

import com.gg.bal_bam.domain.user.dto.TaggedUserRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PostUpdateRequest {

    @NotNull(message = "게시글 내용은 필수입니다.")
    @Size(min = 1, max = 500, message = "게시글 내용은 1자에서 500자 사이여야 합니다.")
    @Schema(description = "게시글 내용", example = "hihi")
    private String content;

    @Schema(description = "태그된 사용자 목록", example = "[{\"userId\": \"4ee3b4d7-2ac6-48ab-bda7-1a204b86d631\", \"username\": \"psw\"}]")
    private List<TaggedUserRequest> taggedUsers; //태그된 사용자 목록

}
