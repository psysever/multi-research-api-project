package com.research2.api.domain.chat.chat_room_members.controller;


import com.research2.api.domain.chat.chat_room_members.service.ChatRoomMemberService;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomResDto;
import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "CHAT NOTIFICATION API", description = "CHAT NOTIFICATION API")
@RequestMapping("/api/chat/notification")
public class ChatRoomMemberController {
    private final ResponseService responseService;
    private final ChatRoomMemberService chatRoomMemberService;


    @Operation(summary = "RECEIVE CHAT OPTION", description = "RECEIVE CHAT OPTION")
    @GetMapping("/enabled")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DATA:SUCCESS", content = @Content(schema = @Schema(implementation = ChatRoomResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<Boolean> findByIsTextNotificationEnabled(String chatRoomId, String mbId) {
        int result = chatRoomMemberService.findByIsTextNotificationEnabled(chatRoomId, mbId);
        return responseService.getSingleResponse(result > 0);
    }


}
