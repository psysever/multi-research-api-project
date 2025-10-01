package com.research1.api.domain.admin.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(title = "Admin Sign Up", description = "Admin Sign Up")
public class CreateAdminDto {

    @NotBlank(message = "username is required")
    @Schema(description = "Admin ID", example = "Admin ID")
    private String username;

    @NotBlank(message = "password is required")
    @Schema(description = "Admin Password", example = "Admin Password")
    private String password;

    @NotBlank(message = "name is required")
    @Schema(description = "Admin Name", example = "Admin Name")
    private String name;


}
