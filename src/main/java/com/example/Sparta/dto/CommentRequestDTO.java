package com.example.Sparta.dto;
import com.example.Sparta.enums.LectureCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class CommentRequestDTO {

    @NotNull
    private int lecture_id;

    @NotNull
    private int user_id;

    @NotNull
    private String text;
}
