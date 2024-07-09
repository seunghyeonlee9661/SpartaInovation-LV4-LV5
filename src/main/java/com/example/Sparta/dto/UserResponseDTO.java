package com.example.Sparta.dto;


import com.example.Sparta.entity.User;
import com.example.Sparta.enums.UserAuthority;
import lombok.Getter;

@Getter
public class UserResponseDTO {
    private String email;
    private String gender;
    private String phone;
    private String address;
    private UserAuthority userAuthority;

    public UserResponseDTO(User user) {
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.userAuthority = user.getAuthority();
    }
}
