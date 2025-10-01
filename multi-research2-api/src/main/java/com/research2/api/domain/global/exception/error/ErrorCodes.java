package com.research2.api.domain.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


public class ErrorCodes {

    @Getter
    @RequiredArgsConstructor
    public enum CommonErrorCode implements ErrorCode {
        BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
        UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
        INVALID_PARAMETER(404, HttpStatus.BAD_REQUEST, "Invalid parameter"),
        INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
        PUSH_MESSAGE_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
        ACCESS_DENIED(403, HttpStatus.FORBIDDEN, "ACCESS_DENIED"),
        INVALID_REFRESH_TOKEN(406, HttpStatus.FORBIDDEN, "INVALID_REFRESH_TOKEN"),
        EXPIRED_ACCESS_TOKEN(407, HttpStatus.UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN"),
        SUBSCRIPTION_PAYMENT(408, HttpStatus.INTERNAL_SERVER_ERROR, "SUBSCRIPTION_PAYMENT"),
        CAN_NOT_FIND_SUBSCRIPTION_PAYMENT(409, HttpStatus.INTERNAL_SERVER_ERROR,
                "CAN_NOT_FIND_SUBSCRIPTION_PAYMENT"),
        DUPLICATED_REGULAR_PAYMENT_INFO(410, HttpStatus.INTERNAL_SERVER_ERROR,
                "DUPLICATED_REGULAR_PAYMENT_INFO"),
        BAD_USE_BLOCK_WORD(420, HttpStatus.valueOf(420), "BAD_USE_BLOCK_WORD"),

        ;

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;

    }

    @Getter
    @RequiredArgsConstructor
    public enum UserErrorCode implements ErrorCode {
        DUPLICATED_USER(1001, HttpStatus.NOT_FOUND, "DUPLICATED_USER"),
        RECOMMEND_USER_CAN_NOT_FIND(1002, HttpStatus.NOT_FOUND, "RECOMMEND_USER_CAN_NOT_FIND"),
        USER_CAN_NOT_FIND(1003, HttpStatus.NOT_FOUND, "USER_CAN_NOT_FIND"),
        USER_DOES_NOT_ENABLED(1005, HttpStatus.NOT_FOUND, "USER_DOES_NOT_ENABLED"),
        CAN_NOT_FIND_DEVICE_INFO(1006, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_DEVICE_INFO"),
        CAN_NOT_FIND_FILE(1007, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_FILE"),
        USER_CREATE_COUPON_ERROR(1008, HttpStatus.INTERNAL_SERVER_ERROR,
                "USER_CREATE_COUPON_ERROR"),
        CHECK_USER_COUPON_SETTING(1009, HttpStatus.INTERNAL_SERVER_ERROR,
                "CHECK_USER_COUPON_SETTING"),
        SHIPPING_INFO_IS_EXIST(1010, HttpStatus.INTERNAL_SERVER_ERROR, "SHIPPING_INFO_IS_EXIST"),
        CAN_NOT_FIND_URINE_CHECK_INFO(1011, HttpStatus.INTERNAL_SERVER_ERROR,
                "CAN_NOT_FIND_URINE_CHECK_INFO"),
        GPT_ERROR(1012, HttpStatus.INTERNAL_SERVER_ERROR,
                "GPT_ERROR"),
        DUPLICATED_NICKNAME(1013, HttpStatus.NOT_FOUND, "DUPLICATED_NICKNAME"),
        DUPLICATED_URINE_CHECK_PET_ID(1014, HttpStatus.NOT_FOUND, "DUPLICATED_URINE_CHECK_PET_ID"),
        COUPON_ZONE_CAN_NOT_FIND(1015, HttpStatus.NOT_FOUND, "COUPON_ZONE_CAN_NOT_FIND"),
        BILLING_FAILED(1016, HttpStatus.NOT_FOUND, "BILLING_FAILED"),
        CAN_NOT_FIND_PAYMENT_INFO(1017, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_PAYMENT_INFO"),
        FAILED_PORTONE_ACCESS_TOKEN(1018, HttpStatus.NOT_FOUND, "FAILED_PORTONE_ACCESS_TOKEN"),
        FAILED_DELETE_BILLING_KEY(1019, HttpStatus.NOT_FOUND, "FAILED_DELETE_BILLING_KEY"),
        CAN_NOT_FIND_ITEM_INFO(1020, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_ITEM_INFO"),
        FAILED_DELETE_PAYMENT_INFO(1021, HttpStatus.NOT_FOUND, "FAILED_DELETE_PAYMENT_INFO"),
        PAYMENT_CANNOT_BE_MADE_ON_THE_SAME_DAY
                (1022, HttpStatus.NOT_FOUND, "PAYMENT_CANNOT_BE_MADE_ON_THE_SAME_DAY"),
        CAN_NOT_FIND_IMP_UID_INFO
                (1023, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_IMP_UID_INFO"),
        CAN_NOT_UPDATE_NICKNAME(1024, HttpStatus.NOT_FOUND, "CAN_NOT_UPDATE_NICKNAME"),
        GPT_NOT_FOUND_CONTENT(1025, HttpStatus.NOT_FOUND, "GPT_NOT_FOUND_CONTENT"),
        GPT_NOT_RESPONSE(1026, HttpStatus.NOT_FOUND, "GPT_NOT_RESPONSE"),
        CART_DELETE_FAILED(1027, HttpStatus.NOT_FOUND, "CART_DELETE_FAILED"),
        NEED_CART_LIST(1028, HttpStatus.NOT_FOUND, "NEED_CART_LIST"),
        CART_CREATE_FAILED(1029, HttpStatus.NOT_FOUND, "CART_CREATE_FAILED"),
        ;

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AdminErrorCode implements ErrorCode {
        ADMIN_CAN_NOT_FIND(1021, HttpStatus.NOT_FOUND, "ADMIN_CAN_NOT_FIND"),
        ADMIN_CAN_NOT_ACCESS(1022, HttpStatus.NOT_FOUND, "ADMIN_CAN_NOT_ACCESS"),
        DUPLICATED_ADMIN_USER(1023, HttpStatus.NOT_FOUND, "DUPLICATED_ADMIN_USER"),
        PASSWORD_DOES_NOT_MATCHED(1024, HttpStatus.NOT_FOUND, "PASSWORD_DOES_NOT_MATCHED"),
        INVOICE_IS_ALREADY_EXIST(1025, HttpStatus.NOT_FOUND, "INVOICE_IS_ALREADY_EXIST"),
        CAN_NOT_FIND_INVOICE(1026, HttpStatus.NOT_FOUND, "CAN_NOT_FIND_INVOICE"),
        INVALID_EXCEL_DATA(1027, HttpStatus.NOT_FOUND, "INVALID_EXCEL_DATA"),
        DOES_NOT_MATCH_COUNT(1028, HttpStatus.NOT_FOUND, "DOES_NOT_MATCH_COUNT"),
        MATERIAL_NAME_DUPLICATED(1029, HttpStatus.NOT_FOUND, "MATERIAL_NAME_DUPLICATED"),
        ;

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ComBoardErrorCode implements ErrorCode {
        ONLY_IN_FIVE_PIC(2001, HttpStatus.NOT_FOUND, "ONLY_IN_FIVE_PIC");

        private final int code;
        private final HttpStatus httpStatus;
        private final String message;
    }


}
