package com.research1.api.global.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title = "PaginationDto (Pagination Request DTO)", description = "Pagination")
public class PaginationDto {

    @Parameter(description = "Page number (starting from 1)")
    @NotNull(message = "pageNo is required")
    @Schema(description = "Page number", example = "Page number for the board")
    private int pageNo = 1;

    @Parameter(description = "Page size (number of rows per page)")
    @NotNull(message = "pageSize is required")
    @Schema(description = "Number of rows to display per page", example = "Number of rows per page")
    private int pageSize = 10;

    public int getPageNo() {
        return (pageNo - 1) * pageSize;
    }
}
