package com.research2.api.domain.global.exception;


import com.research2.api.domain.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

}