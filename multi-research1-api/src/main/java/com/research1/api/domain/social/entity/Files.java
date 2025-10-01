package com.research1.api.domain.social.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "files")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "file", description = "file table")
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "파일 id")
    private int fileId;

    @Schema(description = "file server url")
    private String serverPath;

    @Schema(description = "file name")
    private String fileName;

    @Schema(description = "fileType pub,pri")
    private String fileType;

    @Schema(description = "filePathType")
    private String filePathType;

    @Schema(description = "extension")
    private String extension;

    @Schema(description = "size")
    private Long size;

    @Schema(description = "contentType")
    private String contentType;

    @Schema(description = "del flag", example = "0,1")
    private Integer delFl;


    @Schema(description = "createYmd", example = "2024-10-11 11:00:00")
    private LocalDateTime createYmd;

    @Schema(description = "createId", example = "user_id")
    private Integer createId;


    @Schema(description = "updateYmd", example = "2024-10-11 11:00:00")
    private LocalDateTime updateYmd;

    @Schema(description = "updateId", example = "user_id")
    private Integer updateId;


}
