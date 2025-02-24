package com.sparta.team30.products.service;

import com.sparta.team30.common.external.ExternalApiService;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.domain.ProductDetail;
import com.sparta.team30.products.domain.ProductDetailHistory;
import com.sparta.team30.products.dto.ProductDetailHistoryResponseDto;
import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.repository.ProductDetailHistoryRepository;
import com.sparta.team30.products.repository.ProductDetailRepository;
import com.sparta.team30.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDetailHistoryServiceImpl implements ProductDetailHistoryService {
    private final ProductDetailHistoryRepository productDetailHistoryRepository;
    private final ProductDetailService productDetailService;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ExternalApiService externalApiService;

    @Transactional
    @Override
    public ProductDetailHistoryResponseDto generateContent(UserDetails userDetails, UUID productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품이 존재하지 않습니다."));

        String result = externalApiService.requestProductDetailRecommand(product.getProductName());

        productDetailService.createProductDetail(userDetails, productId, new ProductDetailRequestDto(result));

        ProductDetail productDetail = productDetailRepository.findByProductId(productId);
        ProductDetailHistory productDetailHistory = ProductDetailHistory.builder()
                .productDetail(productDetail)
                .productDetailRequest(product.getProductName())
                .productDetailResponse(result)
                .build();

        productDetailHistoryRepository.save(productDetailHistory);

        return new ProductDetailHistoryResponseDto(result);
    }


}
