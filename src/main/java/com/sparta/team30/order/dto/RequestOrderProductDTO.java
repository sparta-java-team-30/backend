package com.sparta.team30.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "상품명", defaultValue = "햄버거")
    private String productName;
    @Schema(description = "수량", defaultValue = "2")
    private int quantity;
    @Schema(description = "가격", defaultValue = "4000")
    private int price; //개당 가격


}

