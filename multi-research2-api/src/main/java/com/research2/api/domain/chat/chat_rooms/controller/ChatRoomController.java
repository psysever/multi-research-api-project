package com.research2.api.domain.chat.chat_rooms.controller;


import com.research2.api.domain.chat.chat_room_members.service.ChatRoomMemberService;
import com.petnuri.api.domain.chat.chat_rooms.dto.*;
import com.research2.api.domain.chat.chat_rooms.dto.*;
import com.research2.api.domain.chat.chat_rooms.model.ChatRoom;
import com.research2.api.domain.chat.chat_rooms.service.ChatRoomService;
import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "CHAT ROOM API", description = "CHAT ROOM API")
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ResponseService responseService;
    private final ChatRoomMemberService chatRoomMemberService;

    @Operation(summary = "CHAT ROOMS LIST", description = "CHAT ROOMS LIST")
    @GetMapping("/rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CHAT ROOMS LIST", content = @Content(schema = @Schema(implementation = ChatRoomResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public ChatRoomResDto getRoomList(@Valid @ParameterObject GetChatRoomReqDto dto) {
        return chatRoomService.findRoomsByMember(dto);
    }


    @Operation(summary = "CREATE ROOM", description = "CREATE ROOM")
    @PostMapping("/create/room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DATA: TRUE", content = @Content(schema = @Schema(implementation = ChatRoomInfoResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public ChatRoom createRoom(@Valid @RequestBody CreateChatRoomReqDto dto) {

        return chatRoomService.createRoom(dto);
    }


    @Operation(summary = "DELETE CHAT ROOM", description = "DELETE CHAT ROOM")
    @DeleteMapping("/room/del/{roomId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공시 반환 정보", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<Boolean> deleteRoom(@PathVariable String roomId) {
        chatRoomMemberService.removeMembersByRoomId(roomId); // Remove members as well (optional)
        return responseService.getSingleResponse(chatRoomService.deleteRoom(roomId) > 0);
    }


    @Operation(summary = "CHAT ROOM LEAVE", description = "CHAT ROOM LEAVE")
    @DeleteMapping("/room/leave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DATA: SUCCESS", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<Boolean> leaveRoom(@Valid @RequestBody LeaveChatRoomResDto dto) {
        return responseService.getSingleResponse(chatRoomMemberService.leaveMembersByRoomId(dto) > 0);
    }


}
