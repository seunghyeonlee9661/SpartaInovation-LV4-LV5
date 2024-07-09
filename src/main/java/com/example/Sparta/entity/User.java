package com.example.Sparta.entity;

import com.example.Sparta.dto.UserRequestDTO;
import com.example.Sparta.enums.UserAuthority;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/*
이노베이션 캠프 LV-3 : 관리자 Entity
 */

@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @Column(name="email", nullable = false, columnDefinition = "varchar(100)", unique = true)
    private String email;

    @Column(name="password", nullable = false, columnDefinition = "varchar(255)")
    private String password;

    @Column(name="gender", nullable = false, columnDefinition = "varchar(2)")
    private String gender;

    @Column(name="phone", nullable = false, columnDefinition = "varchar(20)")
    private String phone;

    @Column(name="address", nullable = false, columnDefinition = "varchar(255)")
    private String address;

    @Column(name="authority", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserAuthority authority;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();


    public User(UserRequestDTO requestDTO, String password) {
        this.email = requestDTO.getEmail();
        this.password = password;
        this.gender = requestDTO.getGender();
        this.phone = requestDTO.getPhone();
        this.address = requestDTO.getAddress();
        this.authority = requestDTO.getAuthority();
    }
}
