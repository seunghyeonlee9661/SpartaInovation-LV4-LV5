package com.example.Sparta.entity;

import com.example.Sparta.dto.LectureRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @ManyToOne
    @JoinColumn(name="lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    public Comment(Lecture lecture, User user, String text) {
        this.user = user;
        this.lecture = lecture;
        this.text = text;
    }

    public void update(String text) {
        this.text = text;
    }
}
