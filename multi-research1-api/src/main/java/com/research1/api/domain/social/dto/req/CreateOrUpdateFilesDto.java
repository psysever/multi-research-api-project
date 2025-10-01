package com.research1.api.domain.social.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateFilesDto {

    @Schema(description = "ID", example = "")
    private Integer id;

    @Schema(description = "fileType", example = "")
    private String fileType;

    @Schema(description = "filePathType", example = "")
    private String filePathType;

    @Schema(description = "serverPath", example = "")
    private String serverPath;

    @Schema(description = "fileName", example = "")
    private String fileName;

    @Schema(description = "extension", example = "")
    private String extension;

    @Schema(description = "File Size", example = "")
    private Long size;

    @Schema(description = "contentType", example = "")
    private String contentType;
}

