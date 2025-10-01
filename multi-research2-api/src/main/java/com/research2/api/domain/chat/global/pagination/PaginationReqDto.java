package com.research2.api.domain.chat.global.pagination;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title = "PaginationReqDto", description = "PaginationReqDto")
public class PaginationReqDto {

    @Parameter(description = "pageNo START 1")
    @NotNull(message = "pageNo is required")
    @Schema(description = "pageNo", example = "pageNo")
    private int pageNo = 1;

    @Parameter(description = "pageSize Number of rows to display per page")
    @NotNull(message = "pageSize is required")
    @Schema(description = "pageSize", example = "20")
    private int pageSize = 10;

    public int getPageNo() {
        return (pageNo - 1) * pageSize;
    }
}
