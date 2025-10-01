package com.research2.api.domain.auth.controller;


import com.research2.api.domain.auth.dto.req.LoginReqDto;
import com.research2.api.domain.auth.dto.req.ReissueTokensReqDto;
import com.research2.api.domain.auth.entity.Token;
import com.research2.api.domain.auth.service.AuthService;
import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "AUTH API", description = "AUTH API")
public class AuthController {

    private final AuthService authService;
    private final ResponseService responseService;


    @PostMapping("/login")
    @Operation(summary = "login", description = "login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DATA: TOKEN", content = @Content(schema = @Schema(implementation = Token.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "message: USER_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )

    public Token authUser(
            @Valid @RequestBody LoginReqDto loginReqDto) {
        return authService.authUser(loginReqDto);
    }


    @PostMapping("/auth")
    @Operation(summary = "Token Validation", description = "Token Validation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "data: true, false", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "message: INVALID_ACCESS_TOKEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<Boolean> authorization(@Valid @RequestParam String token) {
        Boolean successYn = authService.authorization(token);
        return responseService.getSingleResponse(successYn);
    }


    @PostMapping("/reissueTokens")
    @Operation(summary = "Token reissue upon expiration of authentication", description = "Token reissue upon expiration of authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "data: Token", content = @Content(schema = @Schema(implementation = Token.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "406", description = "message: INVALID_REFRESH_TOKEN", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<Token> reissueTokens(@Valid @RequestBody ReissueTokensReqDto reissueTokensReqDto) {
        Token token = authService.reissueTokens(reissueTokensReqDto);
        return responseService.getSingleResponse(token);
    }

}
