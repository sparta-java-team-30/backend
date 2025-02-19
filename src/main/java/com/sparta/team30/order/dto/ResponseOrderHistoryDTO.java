package com.sparta.team30.order.dto;

import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
//@AllArgsConstructor
public class ResponseOrderHistoryDTO {
    private UUID orderId;
    private Long userId;
    private OrderTypeEnum orderType;
    private LocalDateTime updatedAt;
    private String orderStatus;
    private int price;
    private String paymentType;
    private PaymentTypeEnum paymentStatus;
    private LocalDateTime paymentAt;
    //private String storeName;

    //임시 (음식점 추가 시 삭제 예정)
    public ResponseOrderHistoryDTO(UUID orderId, Long userId, OrderTypeEnum orderType, LocalDateTime updatedAt,
                                   String orderStatus, int price, String paymentType,
                                 PaymentTypeEnum paymentStatus, LocalDateTime paymentAt)
    {
        this.orderId = orderId;
        this.userId = userId;
        this.orderType = orderType;
        this.updatedAt = updatedAt;
        this.orderStatus = orderStatus;
        this.price = price;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.paymentAt = paymentAt;
    }
}
