package com.sparta.team30.products.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductDto {
    private String productName;
    private int price;
    private UUID storeId;
}