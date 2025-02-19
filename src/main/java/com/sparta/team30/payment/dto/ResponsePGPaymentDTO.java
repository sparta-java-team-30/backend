package com.sparta.team30.payment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponsePGPaymentDTO {
    private String company;
    private String paymentType;
    private boolean success;
    private LocalDateTime paymentTime;

    public ResponsePGPaymentDTO(String company, String paymentType, boolean isSuccess) {
        this.company = company;
        this.paymentType = paymentType;
        this.success = isSuccess;
        this.paymentTime = LocalDateTime.now();
    }
}
