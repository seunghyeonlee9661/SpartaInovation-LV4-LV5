package com.example.Sparta.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikeRequestDTO {

    @NotNull
    private int lecture_id;

    @NotNull
    private int user_id;
}
