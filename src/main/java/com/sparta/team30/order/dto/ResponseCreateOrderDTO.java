package com.sparta.team30.order.dto;

import com.sparta.team30.order.domain.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public class ResponseCreateOrderDTO {
    private String message;
    private OrderTypeEnum orderType;

    public ResponseCreateOrderDTO(String message) {
        this.message = message;
    }
}
