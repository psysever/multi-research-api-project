package com.research2.api.domain.chat.chat_messages.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ChatMessage Collection", description = "ChatMessage Collection")
public class ChatMessage {

    @Id
    @Schema(description = "ID", example = "")
    @JsonIgnore
    private String id;

    @NotBlank(message = "chatRoomId is required")
    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;

    @Schema(description = "parentMessageId For Reply Message", example = "")
    private String parentMessageId;

    @NotBlank(message = "senderMbId is required")
    @Schema(description = "senderMbId", example = "")
    private String senderMbId;

    @NotBlank(message = "receiverMbId is required")
    @Schema(description = "receiverMbId", example = "")
    private String receiverMbId;

    @NotBlank(message = "nickName is required")
    @Schema(description = "Sender nickName", example = "")
    private String nickName;

    @Schema(description = "message type : MESSAGE OR IMAGE", example = "")
    private MessageType type;

    @Schema(description = "message", example = "")
    private String message;

    @Schema(description = "attachments", example = "")
    private String attachments;

    @Schema(description = "delFl", example = "")
    private int delFl;

    @Schema(description = "createId", example = "")
    private String createId;

    @Schema(description = "updateId", example = "")
    private String updateId;

    @Schema(description = "createdAt", example = "")
    private LocalDateTime createdAt;

    @Schema(description = "updatedAt", example = "")
    private LocalDateTime updatedAt;


    public enum MessageType {
        MESSAGE, IMAGE, SYSTEM
    }
}