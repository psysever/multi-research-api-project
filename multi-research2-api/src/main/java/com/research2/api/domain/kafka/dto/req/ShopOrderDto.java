package com.research2.api.domain.kafka.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "Shop Order Req", description = "Shop Order Req")
public class ShopOrderDto {

    @NotNull(message = "mbId is required")
    @Schema(description = "mbId", example = "mbId")
    private String mbId;

    @NotNull(message = "orderId is required")
    @Schema(description = "orderId", example = "20291818273")
    private String orderId;


    @Schema(description = "odReceiptPoint", example = "1")
    private int odReceiptPoint;   // USE POINT

    @Schema(description = "Cart Id", example = "1")
    private List<Integer> ctIds;   // CART ID


    @Schema(description = "coupon list", example = "1")
    private List<CouponListDto> couponListReqList;


}