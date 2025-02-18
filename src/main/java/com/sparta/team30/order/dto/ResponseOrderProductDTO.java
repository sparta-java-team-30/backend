package com.sparta.team30.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseOrderProductDTO {
    private String productName;
    private int quantity;
    private int price; //해당 상품 갯수 반영 총 가격
}
