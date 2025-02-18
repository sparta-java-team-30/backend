package com.sparta.team30.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderProductDTO {
    private UUID productId;
    private String productName;
    private int quantity;
    private int price; //개당 가격


}

