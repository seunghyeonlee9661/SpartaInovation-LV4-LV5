package com.example.Sparta.dto.response;

import lombok.Getter;

/* 결과 반환 */
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
