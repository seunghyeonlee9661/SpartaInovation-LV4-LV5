package com.example.Sparta.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotNull
    @Email
    private String username;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "The password must be 8-15 characters long and include at least one letter, one number, and one special character.")
    private String password;
}
