package com.sparta.team30.products.service;

import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.dto.ProductDetailResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface ProductDetailService {
    ProductDetailResponseDto getProductDetail(UUID productId);
    ProductDetailResponseDto createProductDetail(UserDetails userDetails, UUID productId , ProductDetailRequestDto productDetailRequestDto);
    void deleteProductDetail(UserDetails userDetails, UUID productId);
    void updateProductDetail(UUID productId, ProductDetailRequestDto productDetailRequestDto);
}
