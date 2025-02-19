package com.sparta.team30.payment.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ResponseCreatePaymentDTO {
    private UUID paymentId;
    private String message;


    public ResponseCreatePaymentDTO(UUID paymentId, Boolean update) {
        this.paymentId = paymentId;
        if (update) {
            this.message = "결제가 완료되었습니다.";
        }else{
            this.message = "결제가 실패하였습니다.";
        }
    }
}
