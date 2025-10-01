package com.research2.api.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReissueTokensReqDto {

    @NotBlank(message = "refreshToken is required")
    @Schema(description = "refreshToken", example = "refreshToken")
    private String refreshToken;

}