package com.research2.api.domain.chat.chat_messages.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message_reads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ChatMessageRead Collection", description = "ChatMessageRead Collection")
public class ChatMessageRead {
    @Id
    @Schema(description = "id", example = "")
    private String id;

    @Schema(description = "chatRoomId", example = "")
    private String chatRoomId;

    @Schema(description = "Reference message ID", example = "")
    private String chatMessageId;

    @Schema(description = "MB ID", example = "")
    private String mbId;

    @Schema(description = "delFl", example = "")
    private int delFl;

    @Schema(description = "createdAt", example = "")

    private LocalDateTime createdAt;

    @Schema(description = "updatedAt", example = "")
    private LocalDateTime updatedAt;
}
