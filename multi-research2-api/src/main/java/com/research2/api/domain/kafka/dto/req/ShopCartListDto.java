package com.research2.api.domain.kafka.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(title = "Shop Cart List Dto", description = "Shop Cart List  Dto")
public class ShopCartListDto {

    @Schema(description = "orderId", example = "1")
    @JsonIgnore
    private String orderId;

    @Schema(description = "ctId", example = "1")
    @JsonIgnore
    private int ctId;

    @Schema(description = "mbId", example = "1")
    private String mbId;

    @Schema(description = "itemId", example = "1")
    private String itemId;

    @Schema(description = "itemName", example = "ice")
    private String itemName;

    @Schema(description = "itemQty", example = "1")
    private int itemQty;

}
