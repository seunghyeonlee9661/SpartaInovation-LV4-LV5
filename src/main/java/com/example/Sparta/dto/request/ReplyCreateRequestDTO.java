package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/* 대댓글 생성 요청 */
@Getter
public class ReplyCreateRequestDTO {
    @NotNull
    private int comment_id;

    @NotNull
    private int user_id;

    @NotBlank(message = "대댓글 내용은 필수 입니다.")
    private String text;
}
