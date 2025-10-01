package com.research2.api.domain.chat.chat_rooms.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "chat_rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ChatRoom Collection RES DTO", description = "ChatRoom Collection RES DTO")
public class ChatRoomInfoResDto {

    @Id
    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;


    @Schema(description = "CHAT ROOM CREATE SENDER MB ID", example = "")
    private String senderMbId;

    @Schema(description = "CHAT ROOM CREATE receiver MbId", example = "")
    private String receiverMbId;

    @Schema(description = "CHAT ROOM TYPE", example = "")
    private String type;


    @Schema(description = "createId", example = "")
    private String createId;

    @Schema(description = "createdYmd", example = "")
    private LocalDateTime createdYmd;

    @Schema(description = "latestMessage", example = "")
    private String latestMessage;

    @Schema(description = "latestMessageTime", example = "")
    private LocalDateTime latestMessageTime;

    @Schema(description = "unreadCount", example = "")
    private Integer unreadCount;

    @Schema(description = "CHAT MEMBERS LIST", example = "")
    private List<ChatRoomMemberResDto> members;

    @Schema(description = "isTextNotificationEnabled", example = "")
    private boolean isTextNotificationEnabled;
}