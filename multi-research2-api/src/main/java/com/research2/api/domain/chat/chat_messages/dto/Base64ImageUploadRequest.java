package com.research2.api.domain.chat.chat_messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Base64ImageUploadRequest {

    @Schema(description = "base64", example = "")
    private String base64;

    @Schema(description = "fileName", example = "")
    private String fileName;
}

