package com.research2.api.domain.kafka.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.research2.api.domain.chat.global.pagination.PaginationReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Schema(title = "주문 내역 리스트 Req", description = "주문 내역 리스트 Req")
public class OrderListDto extends PaginationReqDto {

    @Schema(description = "검색 유형 PRODUCT:상품명 CODE:상품코드", example = "1")
    private String searchType;

    @Schema(description = "검색어", example = "1")
    private String searchKeyword;

    @Schema(description = "시작일자", example = "1")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @Schema(description = "종료일자", example = "1")
    private LocalDate endDate;

    @Schema(description = "카트상태", example = "취소")
    private String ctStatus;

    @Schema(description = "회원 아이디", example = "1")
    @JsonIgnore
    private String mbId;

    @Schema(description = "이미지 서버 URL", example = "test")
    @JsonIgnore
    private String imageServerUri;

    @Schema(description = "검색 유형", example = "1")
    @JsonIgnore
    private String odId;

    @Schema(description = "메뉴 유형", example = "1")
    private String menuType;
}
