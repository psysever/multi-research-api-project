package com.research2.api.domain.chat.chat_messages.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "NEW MESSAGE DTO", description = "NEW MESSAGE  DTO")
public class ChatRoomSummaryMsgDto {
    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;

    @Schema(description = "latestMessage", example = "")
    private String latestMessage;

    @Schema(description = "latestSendTime", example = "")
    private LocalDateTime latestSendTime;

    @Schema(description = "unreadCount", example = "")
    private int unreadCount;

    @Schema(description = "unreadTotalCount", example = "")
    private int unreadTotalCount;

    @Schema(description = "invalidChatRoom", example = "1")
    private int invalidChatRoom;
}
