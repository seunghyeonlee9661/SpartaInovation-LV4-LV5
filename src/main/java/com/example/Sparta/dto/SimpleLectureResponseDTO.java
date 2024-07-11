package com.example.Sparta.dto;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.enums.LectureCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SimpleLectureResponseDTO {
    private int id;
    private String title;
    private int price;
    private String introduction;
    private LectureCategory category;
    private LocalDateTime regist;

    public SimpleLectureResponseDTO(Lecture lecture){
        this.id = lecture.getId();
        this.title = lecture.getTitle();
        this.price = lecture.getPrice();
        this.introduction = lecture.getIntroduction();
        this.category = lecture.getCategory();
        this.regist = lecture.getRegist();
    }
}
