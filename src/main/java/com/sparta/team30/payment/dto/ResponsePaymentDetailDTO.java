package com.sparta.team30.payment.dto;

import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ResponsePaymentDetailDTO {
    private UUID paymentId;
    private int paymentPrice;
    private String paymentType;
    private PaymentTypeEnum paymentStatus;
    private LocalDateTime updatedAt;
    private String username;

    public ResponsePaymentDetailDTO(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.paymentPrice = payment.getPaymentPrice();
        this.paymentType = payment.getPaymentType();
        this.paymentStatus = payment.getPaymentStatus();
        this.updatedAt = payment.getUpdatedAt();
        this.username = payment.getCreatedBy();
    }
}
