package com.research2.api.domain.chat.global.pagination;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "CustomPaginationReqDto", description = "CustomPaginationReqDto")
public class CustomPaginationReqDto extends PaginationReqDto {

    @Parameter(description = "searchKeyword")
    private String searchKeyword;
}
