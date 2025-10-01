package com.research2.api.domain.global.exception.error;


import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();

    int getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
