package com.example.Sparta.entity;

import com.example.Sparta.dto.LectureRequestDTO;
import com.example.Sparta.entity.Teacher;
import com.example.Sparta.enums.LectureCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name="sparta_lecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @Column(name="title", nullable = false, columnDefinition = "varchar(100)")
    private String title;

    @Column(name="price", nullable = false, columnDefinition = "int")
    private int price;

    @Column(name="introduction", nullable = false, columnDefinition = "TEXT")
    private String introduction;

    @Column(name="category", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private LectureCategory category;

    @ManyToOne
    @JoinColumn(name="teacher_id", nullable = true)
    private Teacher teacher;

    @OneToMany(mappedBy = "lecture", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(name="regist")
    private LocalDateTime regist;

    public Lecture(LectureRequestDTO requestDto, Teacher teacher) {
        this.title = requestDto.getTitle();
        this.price = requestDto.getPrice();
        this.introduction = requestDto.getIntroduction();
        this.category = requestDto.getCategory();
        this.teacher = teacher;
    }

    public void update(LectureRequestDTO requestDto, Teacher teacher) {
        this.title = requestDto.getTitle();
        this.price = requestDto.getPrice();
        this.introduction = requestDto.getIntroduction();
        this.category = requestDto.getCategory();
        this.teacher = teacher;
    }

    public void removeTeacher(){
        this.teacher = null;
    }
}
