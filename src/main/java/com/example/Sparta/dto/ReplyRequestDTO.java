package com.example.Sparta.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReplyRequestDTO {
    @NotNull
    private int lecture_id;

    @NotNull
    private int user_id;

    @NotNull
    private String text;
}
