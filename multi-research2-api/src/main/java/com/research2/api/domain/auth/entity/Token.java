package com.research2.api.domain.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
@AllArgsConstructor
@Schema(title = "Token", description = "Token")
public class Token {

    @Schema(description = "grantType", example = "0")
    private String grantType;

    @Schema(description = "accessToken", example = "0")
    private String accessToken;

    @Schema(description = "refreshToken", example = "0")
    private String refreshToken;
}