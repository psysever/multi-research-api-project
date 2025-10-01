package com.research2.api.domain.chat.chat_messages.controller;


import com.research2.api.domain.chat.chat_messages.dto.Base64ImageUploadRequest;
import com.research2.api.domain.chat.chat_messages.dto.ChatMessageReqDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatMessageResDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatReadReqDto;
import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_messages.service.ChatMessageService;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomResDto;
import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "CHAT MESSAGE API", description = "CHAT MESSAGE API")
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ResponseService responseService;


    @MessageMapping("/send/message")
    public void sendMessage(@Payload ChatMessage chat) {
        chatMessageService.sendMessage(chat);
    }


    //Differences in the characteristics of native HTTP clients across platform
    //Android's OkHttp and Retrofit handle multipart/form-data very efficiently. They transfer files directly as streams, reducing memory usage. They can also reliably upload large images
    @Operation(summary = "CHAT IMAGE MESSAGE (Android: Multipart)", description = "Android")
    @PostMapping(value = "/image/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResponse<String> uploadImageAndroid(
            @RequestPart MultipartFile file
    ) {
        String imageUrl = chatMessageService.sendImageMessage(file);
        return responseService.getSingleResponse(imageUrl);
    }

    //Implementing multipart in URLSession on iOS is relatively complex.
    //Base64 encoding integrates well with JSON, simplifying implementation.
    //Converting images to Base64 in Swift is more intuitive.
    @Operation(summary = "CHAT IMAGE MESSAGE (iOS: Base64)", description = "Upload a Base64 string and generate it as an image.")
    @PostMapping(value = "/image/message/ios")
    public SingleResponse<String> uploadImageIos(
            @RequestBody Base64ImageUploadRequest request
    ) throws IOException {
        String imageUrl = chatMessageService.sendBase64Image(request.getBase64(), request.getFileName());
        return responseService.getSingleResponse(imageUrl);
    }


    @Operation(summary = "CHAT MESSAGE LIST", description = "CHAT MESSAGE LIST")
    @GetMapping("/list/message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CHAT MESSAGE LIST", content = @Content(schema = @Schema(implementation = ChatMessageResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    @Parameter(name = "chatRoomId", description = "chatRoomId")
    public ChatMessageResDto getMessageList(@Valid @ParameterObject ChatMessageReqDto chatMessageReqDto) {
        return chatMessageService.getChatMessageList(chatMessageReqDto);
    }


    @Operation(summary = "READ CHAT MESSAGE", description = "READ CHAT MESSAGE")
    @PostMapping("/read/message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DATA: SUCCESS", content = @Content(schema = @Schema(implementation = ChatRoomResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public SingleResponse<Boolean> markMessagesAsRead(@Valid @RequestBody ChatReadReqDto dto) {
        return responseService.getSingleResponse(chatMessageService.markMessagesAsRead(dto) > 0);
    }

    @Operation(summary = "Get total unread count", description = "Get total unread count")
    @GetMapping("/totalCount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 리스트", content = @Content(schema = @Schema(implementation = ChatRoomResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )
    public int countAllUnreadMessages(String mbId) {
        return chatMessageService.countAllUnreadMessages(mbId);
    }


}