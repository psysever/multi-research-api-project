package com.research2.api.domain.global.dto.res;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {


    private boolean isSuccess;


    private int code;


    private String message;

    public CommonResponse() {
    }

    public CommonResponse(String msg) {
        this.message = msg;
    }
}
