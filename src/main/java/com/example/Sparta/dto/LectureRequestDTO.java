package com.example.Sparta.dto;
import com.example.Sparta.enums.LectureCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class LectureRequestDTO {

    @NotNull
    private String title;

    @NotNull
    @PositiveOrZero
    private int price;

    @NotNull
    private String introduction;

    @NotNull
    private LectureCategory category;

    @NotNull
    private int teacher_id;
}
