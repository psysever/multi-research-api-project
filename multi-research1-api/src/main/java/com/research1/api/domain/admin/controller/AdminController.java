package com.research1.api.domain.admin.controller;

import com.research1.api.domain.admin.dto.res.AdminListResponseDto;
import com.research1.api.global.dto.req.CustomPaginationDto;
import com.research1.api.global.security.jwt.Token;
import com.research1.api.domain.admin.dto.req.AdminLoginDto;
import com.research1.api.domain.admin.dto.req.CreateAdminDto;
import com.research1.api.domain.admin.dto.req.UpdateAdminDto;
import com.research1.api.global.dto.res.SingleResponse;
import com.research1.api.domain.admin.dto.res.AdminResponseDto;
import com.research1.api.global.exception.error.ErrorResponse;
import com.research1.api.global.dto.res.ResponseService;
import com.research1.api.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "a-1. ADMIN API", description = "ADMIN API")
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ResponseService responseService;

    @Operation(summary = "Admin Detail", description = "Retrieve detailed information of an admin user")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/{adminId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin information", content = @Content(schema = @Schema(implementation = AdminResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1001", description = "message: ADMIN_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameter(name = "adminId", description = "Admin ID")
    public AdminResponseDto getAdminInfo(@Valid @PathVariable int adminId) {
        return adminService.getAdminInfo(adminId);
    }

    @Operation(summary = "Admin Login Info", description = "Retrieve information about the logged-in admin user")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/login/info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin information", content = @Content(schema = @Schema(implementation = AdminResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1001", description = "message: ADMIN_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public AdminResponseDto loginAdminInfo() {
        return adminService.loginAdminInfo();
    }

    @PostMapping("/user/login")
    @Operation(summary = "Admin Login", description = "Issue a token using admin username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token value", content = @Content(schema = @Schema(implementation = Token.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1001", description = "message: ADMIN_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1004", description = "message: PASSWORD_DOES_NOT_MATCHED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1006", description = "message: ADMIN_CAN_NOT_ACCESS", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Token> adminLogin(
            @Valid @RequestBody AdminLoginDto adminLoginDto) {
        return adminService.authAdminUser(adminLoginDto);
    }

    @PostMapping("/user/sign_up")
    @Operation(summary = "Admin Registration", description = "Register a new admin user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success 'data': true", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: NEED_USER_NAME, NEED_USER_PASSWORD, NEED_NAME, PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SingleResponse<Boolean> createAdmin(
            @Valid @RequestBody CreateAdminDto createAdminDto) {
        int adminUser = adminService.createAdmin(createAdminDto);
        return responseService.getSingleResponse(adminUser > 0);
    }

    @PutMapping("/user/update")
    @Operation(summary = "Admin Information Update", description = "Update admin user information")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success 'data': true", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SingleResponse<Boolean> updateAdmin(
            @Valid @RequestBody UpdateAdminDto updateAdminDto) {
        int updateAdmin = adminService.updateAdmin(updateAdminDto);
        return responseService.getSingleResponse(updateAdmin > 0);
    }

    @DeleteMapping("/admin/{adminId}")
    @Operation(summary = "Delete Admin", description = "Delete an admin by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success 'data': true", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Admin not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SingleResponse<Boolean> deleteAdmin(@PathVariable int adminId) {
        adminService.deleteAdmin(adminId);
        return responseService.getSingleResponse(true);
    }

    @Operation(summary = "Admin User List Retrieval", description = "Retrieve list of admin users")
    @GetMapping("/user/list")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin information", content = @Content(schema = @Schema(implementation = AdminListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public AdminListResponseDto findAdminList(
            @Valid @ParameterObject CustomPaginationDto customPaginationDto) {
        return adminService.getAdminList(customPaginationDto);
    }

}
