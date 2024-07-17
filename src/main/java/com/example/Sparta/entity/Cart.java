package com.example.Sparta.entity;

import com.example.Sparta.dto.request.CartCreateRequestDTO;
import com.example.Sparta.dto.request.CartUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/*
이노베이션 캠프 LV-3 : 관리자 Entity
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name="count", nullable = false)
    private int count;

    @CreationTimestamp
    @Column(name="date")
    private LocalDateTime date;

    public Cart(User user, Product product, CartCreateRequestDTO cartCreateRequestDTO) {
        this.user = user;
        this.product = product;
        this.count = cartCreateRequestDTO.getCount();
    }

    public void update(CartUpdateRequestDTO cartUpdateRequestDTO){
        this.count = cartUpdateRequestDTO.getCount();
    }
}
