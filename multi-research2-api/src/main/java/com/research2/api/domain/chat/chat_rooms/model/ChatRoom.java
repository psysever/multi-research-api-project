package com.research2.api.domain.chat.chat_rooms.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "chat_rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "CHATROOM COLLECTION", description = "CHATROOM COLLECTION")
public class ChatRoom {

    @Id
    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;


    @Schema(description = "CHATROOM CREATE senderMbId", example = "")
    private String senderMbId;

    @Schema(description = "CHATROOM CREATE RECEIVER ID", example = "")
    private String receiverMbId;


    @Schema(description = "type", example = "")
    private String type;

    @Schema(description = "delFl", example = "")
    private int delFl;

    @Schema(description = "createId", example = "")
    private String createId;

    @Schema(description = "createdYmd", example = "")
    private LocalDateTime createdYmd;
}