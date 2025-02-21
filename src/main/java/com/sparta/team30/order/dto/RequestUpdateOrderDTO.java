package com.sparta.team30.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestUpdateOrderDTO {
    @Schema(description = "요청사항", defaultValue = "요청사항 수정")
    @NotNull(message = "요청사항을 입력해 주세요")
    private String comment;
}
