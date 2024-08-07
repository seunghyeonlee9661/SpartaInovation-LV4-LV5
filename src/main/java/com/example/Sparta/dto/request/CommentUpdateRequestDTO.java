package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/* 댓글 수정 요청 */
@Getter
public class CommentUpdateRequestDTO {
    @NotNull
    private int id;
    
    @NotBlank(message = "댓글 내용은 필수 입니다.")
    private String text;
}
