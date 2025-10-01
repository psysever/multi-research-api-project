package com.research1.api.global.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Schema(title = "ListResponse(리스트 응답)", description = "리스트 응답 데이터")
public class ListResponse<T> extends CommonResponse {

    @Schema(description = "응답 데이터 리스트")
    private List<T> list;

    @Schema(description = "총 데이터 개수")
    private int totalCnt;


    @Data
    @RequiredArgsConstructor
    public static class CommonErrorResponse {

        @Schema(example = "Status Code 500:INTERNAL_SERVER_ERROR")
        private ErrorCode code;
        @Schema(example = "Status Code 404: User can not find,Failed to obtain access token,Unexpected status code Status Code 500: internal server error")
        private ErrorMessage message;

        @Getter
        public enum ErrorCode {
            SNS_CODE_UNEXPECTED("SNS_CODE_UNEXPECTED"),
            INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
            USER_CAN_NOT_FIND("USER_CAN_NOT_FIND"),
            SNS_TOKEN_CAN_NOT_FIND("SNS_TOKEN_CAN_NOT_FIND"),

            // 다른 에러 코드들 정의
            ;

            private final String value;

            ErrorCode(String value) {
                this.value = value;
            }

        }


        @Getter
        public enum ErrorMessage {
            SNS_CODE_UNEXPECTED("Unexpected status code"),
            INTERNAL_SERVER_ERROR("Internal server error"),
            USER_CAN_NOT_FIND("User can not find"),
            SNS_TOKEN_CAN_NOT_FIND("Failed to obtain access token"),

            // 다른 에러 코드들 정의
            ;

            private final String value;

            ErrorMessage(String value) {
                this.value = value;
            }

        }
    }
}
