package com.research2.api.domain.kafka.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.research2.api.domain.chat.global.pagination.PaginationReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Schema(title = "Order History List Req", description = "Order History List Req")
public class OrderListDto extends PaginationReqDto {

    @Schema(description = "search type PRODUCT:product name CODE:product code", example = "1")
    private String searchType;

    @Schema(description = "searchKeyword", example = "1")
    private String searchKeyword;

    @Schema(description = "startDate", example = "1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @Schema(description = "endDate", example = "1")
    private LocalDate endDate;

    @Schema(description = "ctStatus", example = "취소")
    private String ctStatus;

    @Schema(description = "mbId", example = "1")
    @JsonIgnore
    private String mbId;

    @Schema(description = "imageServerUri", example = "test")
    @JsonIgnore
    private String imageServerUri;

    @Schema(description = "odId", example = "1")
    @JsonIgnore
    private String odId;

    @Schema(description = "menuType", example = "1")
    private String menuType;
}
