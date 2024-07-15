package com.example.Sparta.entity;

import com.example.Sparta.dto.request.ProductRequestDTO;
import com.example.Sparta.dto.request.TeacherCreateRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/*
이노베이션 캠프 LV-3 : 관리자 Entity
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(name="price", nullable = false)
    private int price;

    @Column(name="count", nullable = false)
    private int count;

    @Column(name="introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name="category", columnDefinition = "varchar(100)")
    private String category;

    @Column(name="img",columnDefinition = "TEXT")
    private String img;


    public Product(ProductRequestDTO productRequestDTO) {
        this.name = productRequestDTO.getName();
        this.price = productRequestDTO.getPrice();
        this.count = productRequestDTO.getCount();
        this.introduction = productRequestDTO.getIntroduction();
        this.category = productRequestDTO.getCategory();
        this.img = productRequestDTO.getImg();
    }

}
