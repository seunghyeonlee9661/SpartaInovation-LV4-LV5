package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReplyUpdateRequestDTO {

    @NotNull
    private int id;

    @NotBlank(message = "강의 소개는 필수 입니다.")
    private String text;
}
