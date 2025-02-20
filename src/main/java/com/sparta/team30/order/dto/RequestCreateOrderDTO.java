package com.sparta.team30.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCreateOrderDTO {
    private List<RequestOrderProductDTO> productList;
    @Schema(description = "요청사항", defaultValue = "일회용품 필요해요")
    private String comment;
}
