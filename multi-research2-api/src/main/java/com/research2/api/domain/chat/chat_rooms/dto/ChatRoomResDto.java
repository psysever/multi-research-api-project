package com.research2.api.domain.chat.chat_rooms.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@Schema(title = "chatRoomList DTO", description = "chatRoomList DTO")
public class ChatRoomResDto {

    @Schema(description = "chatRoomList", example = "")
    private List<ChatRoomInfoResDto> chatRoomList;

    @Schema(description = "Total Count", example = "1")
    private int totalCnt;
}