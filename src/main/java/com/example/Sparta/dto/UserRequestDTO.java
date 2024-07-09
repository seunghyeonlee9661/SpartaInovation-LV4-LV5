package com.example.Sparta.dto;
import com.example.Sparta.enums.UserAuthority;
import lombok.Getter;

@Getter
public class UserRequestDTO {
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;
    private UserAuthority authority;
}
