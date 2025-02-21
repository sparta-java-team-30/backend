package com.sparta.team30.products.service;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.domain.ProductDetail;
import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.dto.ProductDetailResponseDto;
import com.sparta.team30.products.repository.ProductDetailRepository;
import com.sparta.team30.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final static boolean IS_DELETED_DEFAULT = false;
    private final static boolean IS_DELETED = true;

    @Override
    public ProductDetailResponseDto getProductDetail(UUID productId) {
        ProductDetail result = productDetailRepository.findByProductId(productId);
        if (result == null){
            return new ProductDetailResponseDto("상품 설명이 없습니다.");
        }
        return new ProductDetailResponseDto(result.getProductDetailContent());
    }

    @Transactional
    @Override
    public ProductDetailResponseDto createProductDetail(UserDetails userDetails, UUID productId, ProductDetailRequestDto productDetailRequestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품 없음"));

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);

        if (productDetail != null){
            productDetail.productDetailDelete(IS_DELETED);
            productDetail.delete(userDetails.getUsername());
        }

        ProductDetail detail = ProductDetail.builder()
                .productDetailContent(productDetailRequestDto.getContent())
                .product(product)
                .isDeleted(IS_DELETED_DEFAULT)
                .build();

        ProductDetail saved = productDetailRepository.save(detail);
        return new ProductDetailResponseDto(saved.getProductDetailContent());
    }
}
