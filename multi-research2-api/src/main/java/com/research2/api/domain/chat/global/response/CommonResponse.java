package com.research2.api.domain.chat.global.response;


import lombok.Data;

@Data
public class CommonResponse {


    private boolean isSuccess;


    private int code;


    private String message;

    public CommonResponse() {
    }

    public CommonResponse(String msg) {
        this.message = msg;
    }

    ;

}
