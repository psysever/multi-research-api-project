package com.research2.api.domain.kafka.utill;


// 재시도 제외 대상 예외
public class NonRetryableException extends RuntimeException {
    public NonRetryableException(String message) {
        super(message);
    }

    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }
}
