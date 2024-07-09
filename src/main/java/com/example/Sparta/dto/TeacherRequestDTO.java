package com.example.Sparta.dto;
import com.example.Sparta.enums.LectureCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class TeacherRequestDTO {
    @NotNull
    private String name;

    @NotNull
    @PositiveOrZero
    private int year;

    @NotNull
    private String company;

    @NotNull
    private String phone;

    @NotNull
    private String introduction;
}
