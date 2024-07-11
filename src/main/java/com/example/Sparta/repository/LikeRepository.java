package com.example.Sparta.repository;

import com.example.Sparta.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository   extends JpaRepository<Like, Integer> {
}
