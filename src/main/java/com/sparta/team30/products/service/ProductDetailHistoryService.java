package com.sparta.team30.products.service;

import com.sparta.team30.products.dto.ProductDetailHistoryResponseDto;

import java.util.UUID;

public interface ProductDetailHistoryService {
    ProductDetailHistoryResponseDto generateContent(UUID productId);
}
