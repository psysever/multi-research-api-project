package com.research1.api.domain.admin.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "관리자 로그 생성", description = "관리자 로그 생성")
public class CreateAdminLogDto {

    @Schema(description = "admin userId", example = "1")
    private Integer userId;

    @Schema(description = "admin username", example = "10291")
    private String username;

    @Schema(description = "logType", example = "1")
    private String logType;

    @Schema(description = "logContents", example = "1234")
    private String logContents;

}
