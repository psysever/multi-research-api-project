package com.research1.api.domain.social.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(title = "구독자 검증 RES", description = "구독자 검증 RES")
public class SocialInfoResDto {

    @Schema(description = "구독자 100명 미만 false, 100명 이상 true", example = "true")
    private Boolean subscriberValid;

    @Schema(description = "구독자 인증 채널 URL", example = "www.youtube.com/example")
    private String channelUrl;

    @Schema(description = "채널 타이틀 ", example = "호이tv")
    private String channelTitle;

    @Schema(description = "구독자 수", example = "11")
    private Integer subscriberCount;
}
