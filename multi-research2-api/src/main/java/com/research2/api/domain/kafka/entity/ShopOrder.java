package com.research2.api.domain.kafka.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
@AllArgsConstructor
@Schema(title = "ShopOrder", description = "ShopOrder")
public class ShopOrder {

    @Schema(description = "odReceiptPoint", example = "1000")
    private Integer odReceiptPoint;

    @Schema(description = "mbId", example = "1b13")
    private String mbId;


}