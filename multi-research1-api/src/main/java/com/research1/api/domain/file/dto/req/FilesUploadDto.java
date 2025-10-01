package com.research1.api.domain.file.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FilesUploadDto {

    @JsonIgnore
    @Schema(description = "file", example = "file")
    private MultipartFile file;

    @Schema(description = "filePathType", example = "bankbook,identification,contract")
    private String filePathType;

    @Schema(description = "fileType private,public", example = "public")
    private String fileType;

}
