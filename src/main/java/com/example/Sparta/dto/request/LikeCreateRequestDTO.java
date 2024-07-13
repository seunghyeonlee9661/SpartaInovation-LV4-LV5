package com.example.Sparta.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikeCreateRequestDTO {

    @NotNull
    private int lecture_id;

    @NotNull
    private int user_id;
}
