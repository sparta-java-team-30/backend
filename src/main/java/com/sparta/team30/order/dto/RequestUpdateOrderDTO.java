package com.sparta.team30.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestUpdateOrderDTO {
    @Schema(description = "요청사항", defaultValue = "요청사항 수정")
    private String comment;
    private UUID addressId;
}
