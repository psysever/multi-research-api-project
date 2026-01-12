package com.research2.api.domain.kafka.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Payload Order Generation Event
public class ShopOrderCreatedEventDto {
    @Schema(description = "eventId", example = "1")
    private String eventId;

    @Schema(description = "orderId", example = "1")
    private String orderId;

    @Schema(description = "userId", example = "1")
    private String userId;

    @Schema(description = "occurredAt", example = "1")
    private Instant occurredAt;

    @Schema(description = "odReceiptPoint", example = "1")
    private int odReceiptPoint;   // USE POINT
}
