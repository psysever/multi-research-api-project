package com.research1.api.domain.social.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class FilesDeleteDto {

    @NotNull(message = "fileId is required")
    @Schema(description = "fileId", example = "1")
    private Integer fileId;

    @NotBlank(message = "serverPath is required")
    @Schema(description = "serverPath", example = "")
    private String serverPath;


}
