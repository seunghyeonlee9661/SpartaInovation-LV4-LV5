package com.example.Sparta.dto.request;
import com.example.Sparta.enums.LectureCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class LectureCreateRequestDTO {

    @NotBlank(message = "강의 제목은 필수 입니다.")
    private String title;

    @NotBlank(message = "가격은 필수 항목입니다.")
    @Pattern(regexp = "^\\d$", message = "가격은 숫자로 입력해주세요")  // 11자리 숫자 형식 확인
    private int price;

    @NotBlank(message = "강의 소개는 필수 입니다.")
    private String introduction;

    @NotNull(message = "강의 소개는 필수 입니다.")
    private LectureCategory category;
    
    private int teacher_id;
}
