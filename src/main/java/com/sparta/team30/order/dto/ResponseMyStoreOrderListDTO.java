package com.sparta.team30.order.dto;

import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.payment.dto.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ResponseMyStoreOrderListDTO {
    private UUID orderId;
    private OrderTypeEnum orderType;
    private String orderStatus;
    private int price;
    private LocalDateTime orderDate;
    private List<ResponseOrderProductDTO> OrderProductDTOList;
    private String deliveryAddress;
    private String userName;
    private List<PaymentDTO> payment;
}
