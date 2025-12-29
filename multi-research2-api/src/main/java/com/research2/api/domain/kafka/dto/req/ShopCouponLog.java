package com.research2.api.domain.kafka.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(title = "Shop Order Req", description = "Shop Order Req")
public class ShopCouponLog {


    @Schema(description = "cpId", example = "mbId")
    private String cpId;


    @Schema(description = "cpPrice", example = "20291818273")
    private Integer cpPrice;


    @Schema(description = "mbId", example = "mbId")
    private String mbId;


    @Schema(description = "odId", example = "mbId")
    private String odId;


}