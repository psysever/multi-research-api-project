package com.research1.api.global.dto.req;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "Pagination and Search Keyword", description = "Pagination and Search Keyword")
public class CustomPaginationDto extends PaginationDto {

    @Parameter(description = "Search keyword")
    @Schema(description = "Search keyword")
    private String searchKeyword;
}
