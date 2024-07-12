package com.example.Sparta.entity;

import com.example.Sparta.dto.TeacherRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/*
이노베이션 캠프 LV-3 : 관리자 Entity
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @Column(name="name", nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(name="year", nullable = false, columnDefinition = "int")
    private int year;

    @Column(name="company", nullable = false, columnDefinition = "varchar(50)")
    private String company;

    @Column(name="phone", nullable = false, columnDefinition = "varchar(20)")
    private String phone;

    @Column(name="introduction", nullable = false, columnDefinition = "TEXT")
    private String introduction;

    @OneToMany(mappedBy = "teacher")
    private List<Lecture> lectures = new ArrayList<>();

    public Teacher(TeacherRequestDTO requestDto) {
        this.name = requestDto.getName();
        this.year = requestDto.getYear();
        this.company = requestDto.getCompany();
        this.phone = requestDto.getPhone();
        this.introduction = requestDto.getIntroduction();
    }

    public void update(TeacherRequestDTO requestDto) {
        this.name = requestDto.getName();
        this.year = requestDto.getYear();
        this.company = requestDto.getCompany();
        this.phone = requestDto.getPhone();
        this.introduction = requestDto.getIntroduction();
    }

    @PreRemove
    private void preRemove() {
        for (Lecture lecture : lectures) {
            lecture.removeTeacher(); // 관계 해제
        }
    }
}
