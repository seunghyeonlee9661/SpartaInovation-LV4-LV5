package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequestDTO {

    @NotNull
    private int lecture_id;

    @NotNull
    private int user_id;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String text;
}
