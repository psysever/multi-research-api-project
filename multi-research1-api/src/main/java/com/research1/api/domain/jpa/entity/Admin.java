package com.research1.api.domain.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.research1.api.domain.jpa.enums.AdminStatusTypeEnum;
import com.research1.api.domain.jpa.enums.AdminTypeEnum;
import com.research1.api.global.util.Auditable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
@Schema(title = "Admin Table", description = "Admin Table")
public class Admin extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Admin ID", example = "1")
    private int adminId;

    @Schema(description = "Username", example = "username")
    private String username;

    @Schema(description = "Password", example = "password")
    private String password;


    @Schema(description = "Name", example = "John Doe")
    private String name;

    @Schema(description = "Last Accessed", example = "2023-01-01T00:00:00")
    private LocalDateTime lastAccessedAt;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Admin Type", example = "ADMIN", defaultValue = "ADMIN")
    private AdminTypeEnum userType;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Admin Status", example = "REQUEST_ADMIN", defaultValue = "REQUEST_ADMIN")
    private AdminStatusTypeEnum userStatusType;

    @Schema(description = "Management Permission", example = "1")
    private int managePermission;

    @Schema(description = "User Management Permission", example = "1")
    private int userPermission;

    @Schema(description = "Company Management Permission", example = "0,1")
    private int companyKindPermission;

    @Schema(description = "Dashboard Management Permission", example = "0,1")
    private int dashboardPermission;

    @Schema(description = "Point Management Permission", example = "1")
    private int pointPermission;

    @Schema(description = "Game Management Permission", example = "1")
    private int gamePermission;

    @Schema(description = "Live Broadcast Management Permission", example = "1")
    private int livePermission;

    @Schema(description = "Exchange Permission", example = "1")
    private int exchangePermission;

    @Schema(description = "Point Granting Permission", example = "1")
    private int givePointPermission;

    @Schema(description = "Notice Management Permission", example = "1")
    private int postPermission;

    @Schema(description = "Deletion Flag", example = "0,1")
    private int delFl;

    @Schema(description = "Creator ID", example = "user_id")
    private Integer createId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last Updated Date", example = "2024-10-11 11:00:00")
    private LocalDateTime updateYmd;

    @Schema(description = "Updater ID", example = "user_id")
    private Integer updateId;


}
