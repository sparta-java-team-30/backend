package com.sparta.team30.order.dto;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ResponseOrderDetailsDTO {
    private UUID orderId;
    private OrderTypeEnum orderType;
    private LocalDateTime updatedAt;
    private String comment;
    private String orderStatus;
    private int price;
    //private String storeName;
    private String userPostcode;
    private String userAddress1;
    private String userAddress2;
    private List<ResponseOrderProductDTO> productList;

    public ResponseOrderDetailsDTO(Order order, Address address, List<ResponseOrderProductDTO> productList) {
        this.orderId = order.getOrderId();
        this.orderType = order.getOrderType();
        this.updatedAt = order.getUpdatedAt();
        this.comment = order.getComment();
        this.orderStatus = order.getOrderStatus();
        this.price = order.getPrice();
        //this.storeName =
        this. userPostcode = address.getUserPostcode();
        this.userAddress1 = address.getUserAddress1();
        this.userAddress2 = address.getUserAddress2();
        this.productList = new ArrayList<>(productList);
    }
}
