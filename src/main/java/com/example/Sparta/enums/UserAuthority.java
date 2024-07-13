package com.example.Sparta.enums;

import lombok.Getter;

@Getter
public enum UserAuthority {
    ADMIN(Authority.ADMIN),
    USER(Authority.USER);

    private final String authority;

    UserAuthority(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER"; // 권한 이름 앞에 ROLE_ 접두사 추가
        public static final String ADMIN = "ROLE_ADMIN"; // 권한 이름 앞에 ROLE_ 접두사 추가
    }
}