package com.research2.api.domain.fcm_push_message.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "PublicKeyRes", description = "RSA 공개키 응답")
public class PublicKeyResDto {
    @Schema(description = "SPKI PEM", example = "-----BEGIN PUBLIC KEY----------END PUBLIC KEY-----")
    private String pem;

    @Schema(description = "kid", example = "rsa-2025-08-13")
    private String kid;

    @Schema(description = "JWE alg", example = "RSA-OAEP-256")
    private String alg;
}
