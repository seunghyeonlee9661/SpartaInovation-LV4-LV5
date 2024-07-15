package com.example.Sparta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductRequestDTO{

    @NotBlank(message = "제품 이름은 필수입니다.")
    private String name;

    @NotNull(message = "가격은 필수입니다.")
    private int price;

    @NotNull(message = "재고량은 필수입니다.")
    private int count;

    private String introduction;

    private String category;

    private String img;

}
