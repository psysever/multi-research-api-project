package com.research2.api.domain.cipher.controller;


import com.research2.api.domain.cipher.dto.res.PublicKeyResDto;
import com.research2.api.domain.cipher.config.RSAKeyProvider;
import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cipher")
@Tag(name = "a-0. CIPHER API", description = "CIPHER API")
public class CipherController {


    private final ResponseService responseService;
    private final RSAKeyProvider rsaKeyProvider;


    @Operation(summary = "public-key", description = "public-key")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/public-key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "public-key", content = @Content(schema = @Schema(implementation = PublicKeyResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1021", description = "message: ADMIN_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<PublicKeyResDto> getPublicKey() {
        RSAPublicKey pub = rsaKeyProvider.getPublicKey();
        String pem = toPem(pub);
        String kid = "rsa-" + java.time.LocalDate.now();
        PublicKeyResDto body = PublicKeyResDto.builder()
                .pem(pem)
                .kid(kid)
                .alg("RSA-OAEP-256")
                .build();
        return responseService.getSingleResponse(body);
    }

    private String toPem(RSAPublicKey pub) {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes())
                .encodeToString(pub.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + base64 + "\n-----END PUBLIC KEY-----";
    }
}
