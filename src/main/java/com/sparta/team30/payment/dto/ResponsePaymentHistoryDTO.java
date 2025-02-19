package com.sparta.team30.payment.dto;

import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ResponsePaymentHistoryDTO {
    private UUID paymentId;
    private int paymentPrice;
    private String paymentType;
    private PaymentTypeEnum paymentStatus;
    private LocalDateTime updatedAt;

    public ResponsePaymentHistoryDTO(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.paymentPrice = payment.getPaymentPrice();
        this.paymentType = payment.getPaymentType();
        this.updatedAt = payment.getUpdatedAt();
        this.paymentStatus = payment.getPaymentStatus();
    }

}
