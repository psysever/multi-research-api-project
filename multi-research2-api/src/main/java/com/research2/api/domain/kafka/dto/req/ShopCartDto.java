package com.research2.api.domain.kafka.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@Schema(title = "Shop Cart Dto", description = "Shop Cart Dto")
public class ShopCartDto {


    @Schema(description = "mbId", example = "1")
    private String mbId;

    @Schema(description = "orderType", example = "1: cart 2: direct order")
    private int orderType;

    @Schema(description = "orderType", example = "1: cart 2: direct order")
    private List<ShopCartListDto> ShopCartList;


}
