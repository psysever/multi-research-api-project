package com.research1.api.domain.oauth.controller;


import com.research1.api.domain.oauth.dto.res.SocialUserInfoResDto;
import com.research1.api.domain.oauth.service.SocialPlatFormService;
import com.research1.api.global.dto.res.ResponseService;
import com.research1.api.global.dto.res.SingleResponse;
import com.research1.api.global.exception.CustomException;
import com.research1.api.global.exception.error.ErrorCodes;
import com.research1.api.global.exception.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "a-3. Social login oAuth verification API", description = "Social login oAuth verification")
@RequestMapping("/api/client/inf")
public class SocialPlatformController {

    private final SocialPlatFormService socialPlatFormService;
    private final ResponseService responseService;

    @Operation(summary = "Social login oAuth verification", description = "snsPlatform: youtube,facebook,tiktok,twitch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Social login oAuth verification", content = @Content(schema = @Schema(implementation = SocialUserInfoResDto.class))),
            @ApiResponse(responseCode = "400", description = "message: PARAMETER is incorrect", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "1002", description = "message: USER_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5001", description = "message: SNS_TOKEN_CAN_NOT_FIND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5002", description = "message: SNS_CODE_UNEXPECTED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5007", description = "message: NEED_GOOGLE_AUTH_CODE", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5008", description = "message: PLATFORM_DOES_NOT_SUPPORT", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5009", description = "message: SNS_NEED_CODE", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "message: INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/subscriber/{snsPlatform}")
    public SingleResponse<SocialUserInfoResDto> getSnsPlatformSubscriber(
            @PathVariable String snsPlatform, @RequestParam("code") String code
    ) throws IOException {

        if (code == null || code.isEmpty()) {
            throw new CustomException(ErrorCodes.SnsErrorCode.SNS_NEED_CODE);
        }
        return responseService.getSingleResponse(getSubscriberCount(snsPlatform, code));
    }

    private SocialUserInfoResDto getSubscriberCount(String snsPlatform, String code) throws IOException {
        switch (snsPlatform.toLowerCase()) {
            case "youtube":
                return socialPlatFormService.getYoutubeSubscriberInfo(code);
            case "facebook":
                return socialPlatFormService.getFacebookSubscriberInfo(code);
            case "twitch":
                return socialPlatFormService.getTwitchSubscriberInfo(code);
            case "tiktok":
                return socialPlatFormService.getTiktokSubscriberInfo(code);
            default:
                throw new CustomException(ErrorCodes.SnsErrorCode.PLATFORM_DOES_NOT_SUPPORT);
        }
    }
}
