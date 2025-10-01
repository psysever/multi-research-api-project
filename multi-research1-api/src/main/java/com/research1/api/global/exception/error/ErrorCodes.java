package com.research1.api.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


public class ErrorCodes {

    @Getter
    @RequiredArgsConstructor
    public enum CommonErrorCode implements ErrorCode {
        BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
        INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN"),
        ACCESS_DENIED(403, HttpStatus.FORBIDDEN, "ACCESS_DENIED"),
        INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
        MEDIA_TYPE_ERROR(1404, HttpStatus.NOT_FOUND, "MEDIA_TYPE_ERROR"),
        ;

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;

    }

    @Getter
    @RequiredArgsConstructor
    public enum UserErrorCode implements ErrorCode {
        ADMIN_CAN_NOT_FIND(1001, HttpStatus.NOT_FOUND, "ADMIN_CAN_NOT_FIND"),
        USER_CAN_NOT_FIND(1002, HttpStatus.NOT_FOUND, "USER_CAN_NOT_FIND"),
        DUPLICATED_ADMIN(1003, HttpStatus.NOT_FOUND, "DUPLICATED_ADMIN"),
        PASSWORD_DOES_NOT_MATCHED(1004, HttpStatus.NOT_FOUND, "PASSWORD_DOES_NOT_MATCHED"),
        ADMIN_CAN_NOT_ACCESS(1006, HttpStatus.NOT_FOUND, "ADMIN_CAN_NOT_ACCESS"),
        USER_DOES_NOT_ENABLED(1007, HttpStatus.NOT_FOUND, "USER_DOES_NOT_ENABLED"),
        USER_DOES_NOT_JOIN_GAME(1008, HttpStatus.NOT_FOUND, "USER_DOES_NOT_JOIN_GAME"),
        FILE_UPLOAD_FAIL(1009, HttpStatus.NOT_FOUND, "FILE_UPLOAD_FAIL"),
        FILE_DELETE_FAIL(1010, HttpStatus.NOT_FOUND, "FILE_DELETE_FAIL"),
        FILE_NOT_FOUND(1011, HttpStatus.NOT_FOUND, "FILE_NOT_FOUND"),
        FILE_CAN_NOT_ACCESS(1012, HttpStatus.NOT_FOUND, "FILE_CAN_NOT_ACCESS"),
        FAILED_GET_SIGN_OK_TOKEN(1013, HttpStatus.NOT_FOUND, "FAILD_GET_SIGN_OK_TOKEN"),
        FAILED_TO_MAKE_A_TEMPLATE(1014, HttpStatus.NOT_FOUND, "FAILD_TO_MAKE_A_TEMPLATE"),
        FAILED_TO_MAKE_A_CONTRACT_TEMPLATE(1015, HttpStatus.NOT_FOUND, "FAILED_TO_MAKE_A_CONTRACT_TEMPLATE"),
        FAILED_TO_GET_DOCUMENT(1016, HttpStatus.NOT_FOUND, "FAILED_TO_GET_DOCUMENT");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum InfluencerErrorCode implements ErrorCode {
        EXISTS_INFLUENCER_INFO(2001, HttpStatus.NOT_FOUND, "EXISTS_INFLUENCER_INFO"),
        EXISTS_BLOCK_WORDS(2002, HttpStatus.NOT_FOUND, "EXISTS_BLOCK_WORDS"),
        DUPLICATED_SPONSOR_CODE(2003, HttpStatus.NOT_FOUND, "DUPLICATED_SPONSOR_CODE"),
        DUPLICATED_INFLUENCER_NAME(2004, HttpStatus.NOT_FOUND, "DUPLICATED_INFLUENCER_NAME"),
        EXCHANGE_INFO_CAN_NOT_FIND(2005, HttpStatus.NOT_FOUND, "EXCHANGE_INFO_CAN_NOT_FIND"),
        AMOUNT_MORE_THEN_50000LK(2006, HttpStatus.NOT_FOUND, "AMOUNT_MORE_THEN_50000LK"),
        INFLUENCER_CAN_NOT_FIND(2000, HttpStatus.NOT_FOUND, "INFLUENCER_CAN_NOT_FIND"),
        INFLUENCER_MID_API_ERROR(2007, HttpStatus.NOT_FOUND, "INFLUENCER_MID_API_ERROR"),
        INFLUENCER_EXIST_IN_GAME(2008, HttpStatus.NOT_FOUND, "INFLUENCER_EXIST_IN_GAME"),
        CAN_NOT_FIND_FILE_INFO(2009, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_FILE_INFO"),
        EXCHANGE_REQUEST_ONLY_EACH_MONTH(2010, HttpStatus.NOT_FOUND,
                "EXCHANGE_REQUEST_ONLY_EACH_MONTH"),
        EXISTS_EXCHANGE_REQUEST(2011, HttpStatus.NOT_FOUND,
                "EXISTS_EXCHANGE_REQUEST");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum GameErrorCode implements ErrorCode {
        CAN_NOT_FIND_GAME_DETAILS(3001, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_GAME_DETAILS"),
        GAME_SETTLEMENT_RATES_CAN_NOT_FIND(3002, HttpStatus.NOT_FOUND,
                "GAME_SETTLEMENT_RATES_CAN_NOT_FIND");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PointErrorCode implements ErrorCode {
        CAN_NOT_FIND_POINT_PAYMENT_DETAILS(4001, HttpStatus.NOT_FOUND,
                "CAN_NOT_FIND_POINT_PAYMENT_DETAILS"),
        CAN_NOT_FIND_POINT_DETAILS(4002, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_POINT_DETAILS"),

        AFTER_EXCHANGE_MOUNT_MORE_THEN_50000(4003, HttpStatus.NOT_FOUND,
                "AFTER_EXCHANGE_MOUNT_MORE_THEN_50000"),
        CAN_NOT_FIND_EXCHANGE_REQ_INFO(4004, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_EXCHANGE_REQ_INFO"),
        EXCHANGE_REQ_IS_NOT_EXPECTED_STATUS(4005, HttpStatus.NOT_FOUND,
                "EXCHANGE_REQ_IS_NOT_EXPECTED_STATUS"),
        EXCHANGE_CAN_NOT_REJECT(4006, HttpStatus.NOT_FOUND,
                "EXCHANGE_CAN_NOT_REJECT"),
        PREVIOUS_EXG_REQ_APPLICATION(4007, HttpStatus.NOT_FOUND, "PREVIOUS_EXG_REQ_APPLICATION");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SnsErrorCode implements ErrorCode {
        SNS_TOKEN_CAN_NOT_FIND(5001, HttpStatus.NOT_FOUND, "Failed to obtain access token"),
        SNS_CODE_UNEXPECTED(5002, HttpStatus.NOT_FOUND, "Failed to obtain access token"),
        INVALID_GOOGLE_AUTH_CODE(5003, HttpStatus.NOT_FOUND, "INVALID_GOOGLE_AUTH_CODE"),
        FAILED_OBTAIN_GOOGLE_AC_TOKEN(5004, HttpStatus.NOT_FOUND, "FAILED_OBTAIN_GOOGLE_AC_TOKEN"),
        GOOGLE_CODE_UNEXPECTED(5005, HttpStatus.NOT_FOUND, "GOOGLE_CODE_UNEXPECTED"),
        GOOGLE_STATUS_CODE_UNEXPECTED(5006, HttpStatus.NOT_FOUND, "GOOGLE_STATUS_CODE_UNEXPECTED"),
        NEED_GOOGLE_AUTH_CODE(5007, HttpStatus.NOT_FOUND, "GOOGLE_STATUS_CODE_UNEXPECTED"),
        PLATFORM_DOES_NOT_SUPPORT(5008, HttpStatus.NOT_FOUND, "PLATFORM_DOES_NOT_SUPPORT"),
        SNS_NEED_CODE(5009, HttpStatus.NOT_FOUND, "SNS_NEED_CODE");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

}
