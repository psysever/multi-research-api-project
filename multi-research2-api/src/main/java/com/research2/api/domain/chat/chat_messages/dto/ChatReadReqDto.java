package com.research2.api.domain.chat.chat_messages.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "READ MESSAGE DTO", description = "READ MESSAGE DTO")
public class ChatReadReqDto {

    @NotBlank(message = "chatRoomId is required")
    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;

    @NotBlank(message = "mbId is required")
    @Schema(description = "mbId", example = "")
    private String mbId;

}
