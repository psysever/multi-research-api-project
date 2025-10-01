package com.research1.api.domain.admin.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "Admin Login", description = "Admin Login")
public class AdminLoginDto {

    @NotBlank(message = "username is required")
    @Schema(description = "ADMIN ID", example = "admin")
    private String username;

    @NotBlank(message = "password is required")
    @Schema(description = "ADMIN PASSWORD", example = "1234")
    private String password;

}
