package com.research1.api.domain.oauth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "SocialUserInfoResDto", description = "SocialUserInfoResDto")
public class SocialUserInfoResDto {

    @Schema(description = "subscriberValid under 100 false, 100 up true", example = "true")
    private Boolean subscriberValid;

    @Schema(description = "channelUrl URL", example = "www.youtube.com/example")
    private String channelUrl;

    @Schema(description = "channelTitle ", example = "1244")
    private String channelTitle;

    @Schema(description = "subscriberCount", example = "11")
    private Integer subscriberCount;
}
