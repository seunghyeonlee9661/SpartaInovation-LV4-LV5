package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

/* 장바구니 항목 수정 요청 */
@Getter
public class CartUpdateRequestDTO {

    @NotNull
    private int id;

    @NotNull
    @PositiveOrZero(message = "0개 이상 입력하세요.")
    private int count;
}
