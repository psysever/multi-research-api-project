package com.research2.api.domain.chat.chat_rooms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(title = "CHAT ROOM LEAVE DTO", description = "CHAT ROOM LEAVE DTO")
public class LeaveChatRoomResDto {

    @NotBlank(message = "chatRoomId is required")
    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;

    @NotBlank(message = "mbId is required")
    @Schema(description = "mbId", example = "1")
    private String mbId;

    @NotBlank(message = "nickName is required")
    @Schema(description = "nickName", example = "1")
    private String nickName;
}