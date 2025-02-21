package com.sparta.team30.products.dto;

import lombok.Getter;

@Getter
public class ProductDetailHistoryResponseDto {
    private final String content;

    public ProductDetailHistoryResponseDto(String content) {
        this.content = content;
    }
}
