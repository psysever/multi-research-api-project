package com.research2.api.domain.transaction.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "CretePaymentHistoryDto", description = "CretePaymentHistoryDto")
public class CreatePaymentHistoryDto {

    @Schema(description = "userId")
    private int userId;

    @Schema(description = "EnCrypto cardNumber")
    private String card;

    @Schema(description = "EnCrypto cardNumber")
    private String cardNumber;


}
