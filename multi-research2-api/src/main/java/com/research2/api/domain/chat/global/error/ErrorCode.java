package com.research2.api.domain.chat.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();

    int getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
