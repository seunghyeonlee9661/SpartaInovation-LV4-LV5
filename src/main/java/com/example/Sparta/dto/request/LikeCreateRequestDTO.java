package com.example.Sparta.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/* 좋아요 생성 요청 */
@Getter
public class LikeCreateRequestDTO {

    @NotNull
    private int lecture_id;

    @NotNull
    private int user_id;
}
