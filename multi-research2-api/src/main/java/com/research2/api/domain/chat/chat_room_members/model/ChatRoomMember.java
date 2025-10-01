package com.research2.api.domain.chat.chat_room_members.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "chat_room_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ChatRoomMember Collection", description = "ChatRoomMember Collection")
public class ChatRoomMember {
    @Id
    @Schema(description = "ID", example = "")
    private String id;

    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;

    @Schema(description = "mbId", example = "")
    private String mbId;

    @Schema(description = "nickName", example = "")
    private String nickName;

    @Schema(description = "isTextNotificationEnabled", example = "")
    private boolean isTextNotificationEnabled;

    @Schema(description = "delFl", example = "")
    private int delFl;

    @Schema(description = "createdAt", example = "")
    private LocalDateTime createdAt;

    @Schema(description = "updatedAt", example = "")
    private LocalDateTime updatedAt;

}