package com.sparta.team30.products.controller;

import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.products.dto.ProductDetailHistoryResponseDto;
import com.sparta.team30.products.service.ProductDetailHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductDetailHistoryController {
    private final ProductDetailHistoryServiceImpl productDetailHistoryService;

    @PostMapping("/{productId}/recommend")
    public ResponseEntity<ProductDetailHistoryResponseDto> recommandProductDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("productId") UUID productId){
        ProductDetailHistoryResponseDto productDetailHistoryResponseDto =
                productDetailHistoryService.generateContent(userDetails, productId);

        return ResponseEntity.ok(productDetailHistoryResponseDto);
    }
}
