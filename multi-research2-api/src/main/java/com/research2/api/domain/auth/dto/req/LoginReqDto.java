package com.research2.api.domain.auth.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "LoginReqDto", description = "LoginReqDto")
public class LoginReqDto {

    @NotBlank(message = "username identifier is required")
    @Schema(description = "username", example = "")
    private String username;

    @NotBlank(message = "password is required")
    @Schema(description = "password", example = "")
    private String password;


}
