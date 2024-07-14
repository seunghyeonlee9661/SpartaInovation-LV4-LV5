package com.example.Sparta.handler;

import com.example.Sparta.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* @Valid 에러 처리하는 핸들러 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "입력값이 올바르지 않습니다.";
        return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), errorMessage, null);
    }
}