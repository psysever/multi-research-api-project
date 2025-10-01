package com.research2.api.domain.chat.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "ListResponse", description = "ListResponse")
public class ListResponse<T> extends CommonResponse {

    @Schema(description = "LIST DATA")
    private List<T> list;

    @Schema(description = "totalElements")
    private int totalElements;

}
