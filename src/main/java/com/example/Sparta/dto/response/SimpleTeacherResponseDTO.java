package com.example.Sparta.dto.response;
import com.example.Sparta.entity.Teacher;
import lombok.Getter;

/* 연관관계를 위한 강사 내용 반환 */
@Getter
public class SimpleTeacherResponseDTO {
    private int id;
    private String name;
    private int year;
    private String company;
    private String phone;
    private String introduction;

    public SimpleTeacherResponseDTO(Teacher teacher){
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.year = teacher.getYear();
        this.company = teacher.getCompany();
        this.phone = teacher.getPhone();
        this.introduction = teacher.getIntroduction();
    }
}
