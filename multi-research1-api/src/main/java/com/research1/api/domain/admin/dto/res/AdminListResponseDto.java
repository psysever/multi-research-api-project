package com.research1.api.domain.admin.dto.res;

import com.research1.api.domain.admin.entity.Admin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminListResponseDto {

    @Schema(description = "List of admins", example = "[{...}]")
    private List<Admin> adminList;

    @Schema(description = "Total count", example = "100")
    private int totalCnt;

}
