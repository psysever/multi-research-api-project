package com.research2.api.domain.kafka.dto.req;

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
// 중복 처리 방지를 위한 소비 완료 이벤트 기록
public class ProcessedEventDto {

    @Schema(description = "eventId", example = "1")
    private String eventId;

    @Schema(description = "eventType", example = "1")
    private String eventType;

    @Schema(description = "aggregateId", example = "1")
    private String aggregateId;

    @Schema(description = "processedAt", example = "1")
    private Instant processedAt;
}
