package com.example.Sparta.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private long id;

    @ManyToOne
    @JoinColumn(name="lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    public Like(Lecture lecture, User user) {
        this.user = user;
        this.lecture = lecture;
    }
}
