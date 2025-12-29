package com.research2.api.domain.kafka.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "Coupon List Dto", description = "Coupon List Dto")
public class CouponListDto {
    @Schema(description = "coupon id", example = "1234-1234-1234")
    private String cpId;

    @Schema(description = "coupon price", example = "1000")
    private int cpPrice;
}
