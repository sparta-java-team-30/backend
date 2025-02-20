package com.sparta.team30.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ResponseCreatePaymentDTO {
    private UUID paymentId;
    @Schema(description = "결제 메시지", defaultValue = "결제가 완료되었습니다.")
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
