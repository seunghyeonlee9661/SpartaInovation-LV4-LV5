package com.example.Sparta.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentUpdateDTO {
    @NotNull
    private String text;
}
