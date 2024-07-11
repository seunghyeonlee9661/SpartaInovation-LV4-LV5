package com.example.Sparta.dto;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.enums.LectureCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LectureResponseDTO {
    private int id;
    private String title;
    private int price;
    private String introduction;
    private LectureCategory category;
    private LocalDateTime regist;
    private SimpleTeacherResponseDTO teacher;
    private List<CommentResponseDTO> comments;

    public LectureResponseDTO(Lecture lecture){
        this.id = lecture.getId();
        this.title = lecture.getTitle();
        this.price = lecture.getPrice();
        this.introduction = lecture.getIntroduction();
        this.category = lecture.getCategory();
        this.teacher = new SimpleTeacherResponseDTO(lecture.getTeacher());
        this.regist = lecture.getRegist();
        this.comments = lecture.getComments().stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }
}
