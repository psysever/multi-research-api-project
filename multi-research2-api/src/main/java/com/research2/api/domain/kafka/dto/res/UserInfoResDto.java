package com.research2.api.domain.kafka.dto.res;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "UserInfoResDto", description = "UserInfoResDto")
public class UserInfoResDto {

    @Schema(description = "mbId", example = "1")
    private int mbId;

    @Schema(description = "username", example = "username")
    private String username;

    @JsonIgnore
    @Schema(description = "password", example = "password")
    private String password;


}
