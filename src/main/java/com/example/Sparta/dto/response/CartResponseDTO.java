package com.example.Sparta.dto.response;

import com.example.Sparta.entity.Cart;
import com.example.Sparta.entity.Product;
import com.example.Sparta.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CartResponseDTO {
    private int id;
    private Product product;
    private int count;
    private LocalDateTime date;

    public CartResponseDTO(Cart cart) {
        this.id = cart.getId();
        this.product = cart.getProduct();
        this.count = cart.getCount();
        this.date = cart.getDate();
    }
}
