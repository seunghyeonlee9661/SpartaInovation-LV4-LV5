package com.example.Sparta.dto.request;
import com.example.Sparta.enums.UserAuthority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserCreateRequestDTO {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "성별은 필수 항목입니다.")
    private String gender;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{11}$", message = "전화번호는 11자리 숫자여야 합니다.")
    private String phone;

    @NotBlank(message = "주소는 필수 항목입니다.")
    private String address;

    @NotNull(message = "권한은 필수 항목입니다.")
    private UserAuthority authority;
}
