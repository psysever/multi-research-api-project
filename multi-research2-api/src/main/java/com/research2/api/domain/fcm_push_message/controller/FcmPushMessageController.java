package com.research2.api.domain.fcm_push_message.controller;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.nimbusds.oauth2.sdk.SuccessResponse;
import com.research2.api.domain.fcm_push_message.dto.req.FcmSendMessageDto;
import com.research2.api.domain.fcm_push_message.service.FcmPushMessageService;

import com.research2.api.domain.global.dto.res.ResponseService;
import com.research2.api.domain.global.dto.res.SingleResponse;


import com.research2.api.domain.global.exception.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "d-2. FCM 푸시메세지 API", description = "a-5. FCM 푸시메세지 API 명세서")
@RequestMapping("/api/fcm")
public class FcmPushMessageController {

    private final FcmPushMessageService fcmPushMessageService;
    private final ResponseService responseService;


    @Operation(summary = "sendSinglePushMessage", description = "sendSinglePushMessage")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/single/send")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "1006", description = "message: CAN_NOT_FIND_DEVICE_INFO", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )

    public SingleResponse<Boolean> sendSinglePushMessage(@RequestBody @Validated FcmSendMessageDto fcmSendMessageDto)
            throws FirebaseMessagingException {
        fcmPushMessageService.sendSinglePushMessage(fcmSendMessageDto);
        return responseService.getSingleResponse(true);
    }

    @Operation(summary = "FCM PUSH sendMultiPushMessage", description = "CM PUSH sendMultiPushMessage")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/multi/send")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "1006", description = "message: CAN_NOT_FIND_DEVICE_INFO", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    }
    )

    public SingleResponse<Boolean> sendMultiPushMessage(@RequestBody @Validated FcmSendMessageDto fcmSendMessageDto)
            throws FirebaseMessagingException {
        fcmPushMessageService.sendMultiPushMessage(fcmSendMessageDto);
        return responseService.getSingleResponse(true);
    }

}
