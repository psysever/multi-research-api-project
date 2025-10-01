package com.research1.api.global.dto.res;

import java.util.List;

import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;

@EqualsAndHashCode(callSuper = false)
@Service
public class ResponseService {

    public <T> SingleResponse<T> getSingleResponse(T data) {
        SingleResponse<T> res = new SingleResponse<>();
        res.setData(data);
        res.setCode(200);
        res.setMessage("Success");
        res.setSuccess(true);
        return res;
    }

    public <T> ListResponse<T> getListResponse(List<T> list) {
        ListResponse<T> res = new ListResponse<>();
        res.setList(list);
        res.setCode(200);
        res.setMessage("Success");
        res.setSuccess(true);
        return res;
    }

    public <T> ListResponse<T> getListPageResponse(List<T> list, int totalCnt) {
        ListResponse<T> res = new ListResponse<>();
        res.setList(list);
        res.setCode(200);
        res.setTotalCnt(totalCnt); // totalElements 값을 설정합니다.
        res.setMessage("Success");
        res.setSuccess(true);
        return res;
    }

    public CommonResponse getSuccessResponse() {
        CommonResponse res = new CommonResponse();
        res.setSuccess(true);
        res.setCode(200);
        res.setMessage("Success");
        return res;
    }

    public CommonResponse getFailResponse(String msg) {
        CommonResponse res = new CommonResponse(msg);
        res.setSuccess(false);
        res.setCode(-1);
        res.setMessage(msg);
        return res;
    }

}
