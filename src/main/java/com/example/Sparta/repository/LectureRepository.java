package com.example.Sparta.repository;

import com.example.Sparta.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    Page<Lecture> findByCategoryOrderByRegistDesc(String category, Pageable pageable);
}
