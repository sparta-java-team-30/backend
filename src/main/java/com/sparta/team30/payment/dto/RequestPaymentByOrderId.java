package com.sparta.team30.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestPaymentByOrderId {
    private UUID orderId;
    @Schema(description = "결제종류", defaultValue = "카드결제")
    @NotNull(message = "결제수단을 선택해 주세요")
    private String paymentType;
}
