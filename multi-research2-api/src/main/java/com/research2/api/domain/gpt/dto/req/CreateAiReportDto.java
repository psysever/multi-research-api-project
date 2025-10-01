package com.research2.api.domain.gpt.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "GPT REPORT", description = "GPT REPORT")
public class CreateAiReportDto {

    @Schema(description = "userId")
    private int userId;
    
    @Schema(description = "managementCommend")
    private String managementCommend;

    @Schema(description = "subjectCommend")
    private String subjectCommend;

    @Schema(description = "entireCommend")
    private String entireCommend;
}
