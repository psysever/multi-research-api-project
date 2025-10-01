package com.research2.api.domain.chat.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ErrorCodes {

    @Getter
    @RequiredArgsConstructor
    public enum CommonErrorCode implements ErrorCode {
        BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
        UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
        ACCESS_DENIED(403, HttpStatus.FORBIDDEN, "ACCESS_DENIED"),
        INVALID_REFRESH_TOKEN(406, HttpStatus.FORBIDDEN, "INVALID_REFRESH_TOKEN"),
        INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
        EXPIRED_ACCESS_TOKEN(407, HttpStatus.UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN"),
        INVALID_API_KEY(404, HttpStatus.UNAUTHORIZED, "INVALID_API_KEY"),
        BAD_USE_BLOCK_WORD(420, HttpStatus.valueOf(420), "BAD_USE_BLOCK_WORD"),
        ;

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;

    }

    @Getter
    @RequiredArgsConstructor
    public enum UserErrorCode implements ErrorCode {
        CAN_NOT_GET_FIREBASE_TOKEN(1001, HttpStatus.NOT_FOUND, "CAN_NOT_GET_FIREBASE_TOKEN"),
        CAN_NOT_GET_DEVICE_INFO(1002, HttpStatus.NOT_FOUND, "CAN_NOT_GET_DEVICE_INFO"),
        PUSH_MESSAGE_ERROR(1003, HttpStatus.NOT_FOUND, "PUSH_MESSAGE_ERROR"),
        ;

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum GoodsFlowErrorCode implements ErrorCode {
        RECEIVE_TRACE_RESULT_ERROR(1101, HttpStatus.NOT_FOUND, "RECEIVE_TRACE_RESULT_ERROR"),
        RECEIVE_TRANS_RESULT_ERROR(1102, HttpStatus.NOT_FOUND, "RECEIVE_TRANS_RESULT_ERROR");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

}
