package com.sparta.team30.products.service;

import com.sparta.team30.products.dto.ProductDetailHistoryResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface ProductDetailHistoryService {
    ProductDetailHistoryResponseDto generateContent(UserDetails userDetails, UUID productId);
}
