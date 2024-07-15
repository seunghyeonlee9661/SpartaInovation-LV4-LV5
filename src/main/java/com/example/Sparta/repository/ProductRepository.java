package com.example.Sparta.repository;

import com.example.Sparta.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository   extends JpaRepository<Product, Integer> {
}

