package com.research2.api.domain.chat.chat_messages.dto;


import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "CHAT MESSAGE LIST RES DTO", description = "CHAT MESSAGE LIST RES DTO")
public class ChatMessageResDto {
    @Schema(description = "chatMessageList", example = "")
    List<ChatMessage> chatMessageList;

    @Schema(description = "totalCnt", example = "")
    int totalCnt;
}
