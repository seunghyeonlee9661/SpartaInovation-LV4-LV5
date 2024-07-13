package com.example.Sparta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class TeacherCreateRequestDTO {

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotNull(message = "연차는 필수 항목입니다.")
    @PositiveOrZero(message = "연차는 0 이상의 값이어야 합니다.")
    private Integer year;  // int 대신 Integer로 변경하여 null 검사를 가능하게 합니다.

    @NotBlank(message = "회사는 필수 항목입니다.")
    private String company;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{11}$", message = "전화번호는 11자리 숫자여야 합니다.")  // 11자리 숫자 형식 확인
    private String phone;

    @NotBlank(message = "자기소개는 필수 항목입니다.")
    private String introduction;
}