package com.example.Sparta.dto.response;

import com.example.Sparta.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponseDTO {
    private int id;
    private String name;
    private int price;
    private int count;
    private String introduction;
    private String category;
    private String img;

    public ProductResponseDTO(Product product, String img) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.count = product.getCount();
        this.introduction = product.getIntroduction();
        this.category = product.getCategory();
        this.img = img;
    }
}
