package com.example.Sparta.entity;

import com.example.Sparta.dto.request.CommentCreateRequestDTO;
import com.example.Sparta.dto.request.CommentUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(name="regist")
    private LocalDateTime regist;

    @OneToMany(mappedBy = "comment", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    public Comment(Lecture lecture, User user, CommentCreateRequestDTO commentCreateRequestDTO) {
        this.user = user;
        this.lecture = lecture;
        this.text = commentCreateRequestDTO.getText();
    }

    public void update(CommentUpdateRequestDTO commentUpdateRequestDTO) {
        this.text = commentUpdateRequestDTO.getText();
    }
}
