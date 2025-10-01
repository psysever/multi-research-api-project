package com.research2.api.domain.chat.chat_messages.dto;


import com.research2.api.domain.chat.global.pagination.CustomPaginationReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "CHAT MESSAGE LIST REQ DTO", description = "CHAT MESSAGE LIST REQ DTO")
public class ChatMessageReqDto extends CustomPaginationReqDto {

    @NotBlank(message = "chatRoomId is required")
    @Schema(description = "chatRoomId", example = "")
    String chatRoomId;
}
