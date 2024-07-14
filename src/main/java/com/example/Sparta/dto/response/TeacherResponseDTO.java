package com.example.Sparta.dto.response;
import com.example.Sparta.entity.Teacher;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/* 강사 내용 반환 */
@Getter
public class TeacherResponseDTO {
    private int id;
    private String name;
    private int year;
    private String company;
    private String introduction;
    private List<SimpleLectureResponseDTO> lectures;

    public TeacherResponseDTO(Teacher teacher){
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.year = teacher.getYear();
        this.company = teacher.getCompany();
        this.introduction = teacher.getIntroduction();
        this.lectures = teacher.getLectures().stream().map(SimpleLectureResponseDTO::new).collect(Collectors.toList());
    }
}
