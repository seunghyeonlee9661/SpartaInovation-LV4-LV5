package com.example.Sparta.enums;

import lombok.Getter;

@Getter
public enum UserAuthority {
    ADMIN(Authority.USER),
    USER(Authority.ADMIN);

    private final String authority;

    UserAuthority(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }

}
