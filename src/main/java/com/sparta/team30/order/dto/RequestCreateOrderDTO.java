package com.sparta.team30.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCreateOrderDTO {
    @NotNull(message = "상품을 하나 이상 선택해주세요")
    private List<RequestOrderProductDTO> productList;
    @Schema(description = "요청사항", defaultValue = "일회용품 필요해요")
    private String comment;
}
