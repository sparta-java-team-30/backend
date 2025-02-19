package com.sparta.team30.products.service;

import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.dto.ProductDetailResponseDto;

import java.util.UUID;

public interface ProductDetailService {
    ProductDetailResponseDto getProductDetail(UUID productId);
    ProductDetailResponseDto createProductDetail(UUID productId ,ProductDetailRequestDto productDetailRequestDto);
}
