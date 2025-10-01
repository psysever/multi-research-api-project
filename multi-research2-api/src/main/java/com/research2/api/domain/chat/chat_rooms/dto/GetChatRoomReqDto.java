package com.research2.api.domain.chat.chat_rooms.dto;

import com.research2.api.domain.chat.global.pagination.CustomPaginationReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Schema(title = "CHAT ROOM LIST DTO", description = "CHAT ROOM LIST DTO")
public class GetChatRoomReqDto extends CustomPaginationReqDto {

    @Schema(description = "mbID", example = "1")
    private String mbId;

    @Schema(description = "PRIVATE , ALL", example = "ALL")
    private String chatType;
}