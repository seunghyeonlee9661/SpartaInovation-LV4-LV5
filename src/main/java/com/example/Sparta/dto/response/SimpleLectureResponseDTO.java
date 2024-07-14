package com.example.Sparta.dto.response;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.enums.LectureCategory;
import lombok.Getter;

import java.time.LocalDateTime;

/* 연관관계를 위한 강의 내용 반환 */
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
