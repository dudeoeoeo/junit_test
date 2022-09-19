package com.example.junitproject.web.dto.handler;

import com.example.junitproject.web.dto.response.CMRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiException(RuntimeException e) {
        final CMRespDto<?> body = CMRespDto.builder().code(-1).msg(e.getMessage()).build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
