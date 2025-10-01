package com.research1.api.domain.file.controller;

import com.research1.api.domain.admin.dto.res.AdminResponseDto;
import com.research1.api.domain.file.dto.req.FilesDeleteDto;
import com.research1.api.domain.file.dto.req.FilesUploadDto;
import com.research1.api.domain.file.entity.Files;
import com.research1.api.domain.file.service.FilesService;
import com.research1.api.global.dto.res.ResponseService;
import com.research1.api.global.dto.res.SingleResponse;
import com.research1.api.global.exception.error.ErrorResponse;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "a-2. FILE API", description = "FILE API")
@RequestMapping("/api/files")
public class FilesController {

    private final FilesService filesService;
    private final ResponseService responseService;

    @Operation(summary = "GET FILE INFO", description = "REDIRECT FILE")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{fileId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File information", content = @Content(schema = @Schema(implementation = AdminResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1011", description = "message: FILE_NOT_FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameter(name = "fileId", description = "fileId")
    public ResponseEntity<Void> redirectFile(@Valid @PathVariable int fileId) {
        Files file = filesService.getFile(fileId);

        URI target = UriComponentsBuilder
                .fromHttpUrl(file.getServerPath())
                .path(file.getServerPath())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(target)
                .build();
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "upload File", description = "upload File")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 ", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "message: UNAUTHORIZED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "2002", description = "message: EXISTS_BLOCK_WORDS", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SingleResponse<Boolean> uploadFile(
            @Valid @RequestPart FilesUploadDto filesUploadDto, @RequestPart MultipartFile file
    ) throws IOException {
        filesUploadDto.setFile(file);
        int fileId = filesService.uploadFile(filesUploadDto);
        return responseService.getSingleResponse(fileId > 0);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "Delete File", description = "Delete File")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success 'data': true", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "1010", description = "FILE_DELETE_FAIL", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public SingleResponse<Boolean> deleteFile(@Valid @RequestBody FilesDeleteDto filesDeleteDto) {
        filesService.deleteFile(filesDeleteDto);
        return responseService.getSingleResponse(true);
    }


}
