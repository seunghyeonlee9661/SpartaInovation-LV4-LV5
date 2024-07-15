package com.example.Sparta.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartCreateRequestDTO {

    @NotNull
    private int user_id;

    @NotNull
    private int product_id;

    @NotNull
    private int count;
}
