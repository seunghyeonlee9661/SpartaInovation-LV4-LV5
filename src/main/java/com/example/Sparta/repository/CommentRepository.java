package com.example.Sparta.repository;

import com.example.Sparta.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByLectureId(int lectureId);
}
