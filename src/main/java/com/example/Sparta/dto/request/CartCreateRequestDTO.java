package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/* 장바구니 항목 생성 요청 */
@Getter
public class CartCreateRequestDTO {

    @NotNull
    private int user_id;

    @NotNull
    private int product_id;

    @NotNull
    private int count;
}
