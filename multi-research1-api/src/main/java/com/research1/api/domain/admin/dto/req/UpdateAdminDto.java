package com.research1.api.domain.admin.dto.req;

import com.research1.api.domain.admin.enums.AdminStatusTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title = "Admin Info Update", description = "Request DTO for updating admin information")
public class UpdateAdminDto {

    @NotNull(message = "adminId is required")
    @Schema(description = "Admin ID", example = "1")
    private int adminId;

    @Schema(description = "Admin password", example = "adminPassword123")
    private String password;

    @Schema(description = "Admin name", example = "John Doe")
    private String name;


    @Schema(description = "Admin status", example = "REQUEST_ADMIN, APPROVAL_ADMIN")
    private AdminStatusTypeEnum userStatusType;

    @Schema(description = "Management permission (0: no, 1: yes)", example = "0")
    private Integer managePermission;

    @Schema(description = "User management permission (0: no, 1: yes)", example = "1")
    private Integer userPermission;

    @Schema(description = "Company management permission (0: no, 1: yes)", example = "1")
    private Integer companyKindPermission;

    @Schema(description = "Dashboard management permission (0: no, 1: yes)", example = "1")
    private Integer dashboardPermission;

    @Schema(description = "Point management permission - LK history (0: no, 1: yes)", example = "1")
    private Integer pointPermission;

    @Schema(description = "Point management permission - Exchange rights (0: no, 1: yes)", example = "1")
    private Integer exchangePermission;

    @Schema(description = "Point management permission - Point issuance rights (0: no, 1: yes)", example = "1")
    private Integer givePointPermission;

    @Schema(description = "Game management permission (0: no, 1: yes)", example = "1")
    private Integer gamePermission;

    @Schema(description = "Live broadcast management permission (0: no, 1: yes)", example = "1")
    private Integer livePermission;

    @Schema(description = "Notice management permission (0: no, 1: yes)", example = "1")
    private Integer postPermission;
}
