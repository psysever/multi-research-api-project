package com.research2.api.domain.fcm_push_message.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "DeviceInfo", description = "Device Type - I = 'IOS' , A = 'ANDROID',O = 'OTHER', userID")
public class DeviceInfo {

    @Schema(description = "userId")
    private int userId;

    @Schema(description = "deviceId")
    private int deviceId;

    @Schema(description = "deviceType")
    private String deviceType;

    @Schema(description = "deviceToken")
    private String deviceToken;


    @Schema(description = "lastLoginYmd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastLoginYmd;


}


