package com.research2.api.domain.chat.chat_rooms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data

@Schema(title = "CHAT ROOM CREATE DTO", description = "CHAT ROOM CREATE DTO")
public class CreateChatRoomReqDto {


    @NotBlank(message = "mbId is required ")
    @Schema(description = "CHAT ROOM CREATE MbID", example = "CHAT ROOM CREATE MbID")
    private String mbId;


    @NotBlank(message = "attentionMbId is required ")
    @Schema(description = "CHAT ROOM INVITED mbId", example = "CHAT ROOM INVITED mbId")
    private String attentionMbId;


}