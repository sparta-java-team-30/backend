package com.sparta.team30.payment.dto;

import com.sparta.team30.payment.domain.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PaymentDTO {
    private UUID paymentId;
    private int paymentPrice;
    private String paymentType;
    private int paymentCount;
    private PaymentTypeEnum paymentStatus;
}
