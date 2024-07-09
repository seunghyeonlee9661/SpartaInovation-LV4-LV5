package com.example.Sparta.dto;
import com.example.Sparta.enums.UserAuthority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "The password must be 8-15 characters long and include at least one letter, one number, and one special character.")
    private String gender;

    @NotNull
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private UserAuthority authority;
}
