package com.research2.api.domain.chat.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor

@Schema(title = "SingleResponse", description = "SINGLE DATA")
public class SingleResponse<T> extends CommonResponse {

    @Schema(description = "Single Response Data", example = "true")
    private T data;
}
