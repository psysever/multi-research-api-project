package com.research2.api.domain.global.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Schema(title = "SingleResponse", description = "SingleResponse")
public class SingleResponse<T> extends CommonResponse {

    @Schema(description = "Single Response Data", example = "true")
    private T data;
}
