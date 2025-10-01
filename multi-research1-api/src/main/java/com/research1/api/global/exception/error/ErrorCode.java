package com.research1.api.global.exception.error;


import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();

    int getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
