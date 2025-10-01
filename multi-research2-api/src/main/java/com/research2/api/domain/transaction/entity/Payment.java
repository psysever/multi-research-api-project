package com.research2.api.domain.transaction.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(title = "Payment", description = "Payment")
public class Payment {

    @Schema(description = "userId")
    private int userId;

    @Schema(description = "EnCrypto cardNumber")
    private String card;

    @Schema(description = "EnCrypto cardNumber")
    private String cardNumber;


}
