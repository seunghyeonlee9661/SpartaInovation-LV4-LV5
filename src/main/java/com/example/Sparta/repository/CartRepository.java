package com.example.Sparta.repository;

import com.example.Sparta.entity.Cart;
import com.example.Sparta.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(int id);
}
