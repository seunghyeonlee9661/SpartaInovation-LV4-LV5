package com.example.Sparta.dto;

import lombok.Getter;

@Getter
public class ResponseDTO {
    private int status;
    private String message;
    private Object data;

    public ResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
