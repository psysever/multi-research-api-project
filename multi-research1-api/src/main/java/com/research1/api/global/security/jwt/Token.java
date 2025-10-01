package com.research1.api.global.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for sending tokens to the client
 * <p>
 * grantType: The authentication type for JWT. Here, "Bearer" is used.
 * It is the prefix type added to the HTTP header.
 * <p>
 * Author: rimsong
 */

@Builder
@Data
@AllArgsConstructor
@Schema(title = "Token", description = "Token DTO")
public class Token {

    @Schema(description = "Authentication type for JWT", example = "Bearer")
    private String grantType; // Authentication type for JWT, typically "Bearer", used as a prefix in HTTP headers

    @Schema(description = "Access token", example = "access token string")
    private String accessToken;

    @Schema(description = "Refresh token", example = "refresh token string")
    private String refreshToken;
}
