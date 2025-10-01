package com.research2.api.domain.fcm_push_message.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(title = "FcmSendMessageDto", description = "FcmSendMessageDto")
public class FcmSendMessageDto {

    @NotNull(message = "userId is required")
    @Schema(description = "MEMBER ID", example = "1")
    private int userId;

    @Schema(description = "title", example = "title")
    private String title;

    @Schema(description = "body", example = "1")
    private String body;

    @Schema(description = "image", example = "1")
    private String image;
}
