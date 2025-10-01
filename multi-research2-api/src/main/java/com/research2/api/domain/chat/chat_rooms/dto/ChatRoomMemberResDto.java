package com.research2.api.domain.chat.chat_rooms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(title = "ChatRoomMember RES DTO", description = "ChatRoomMember RES DTO")
public class ChatRoomMemberResDto {

    @Schema(description = "MB ID", example = "")
    private String mbId;

    @Schema(description = "nickName", example = "")
    private String nickName;
    
    @Schema(description = "userProfileImageUrl", example = "test.jpg")
    private String userProfileImageUrl;

    @Schema(description = "isTextNotificationEnabled", example = "")
    @JsonIgnore
    private boolean isTextNotificationEnabled;

    @Schema(description = "delFl", example = "")
    @JsonIgnore
    private int delFl;

}