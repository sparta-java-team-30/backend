package com.sparta.team30.payment.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestPaymentByOrderId {
    private UUID orderId;
    private String paymentType;
}
