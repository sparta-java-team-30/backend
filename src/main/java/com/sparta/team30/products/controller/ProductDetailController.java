package com.sparta.team30.products.controller;

import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.dto.ProductDetailResponseDto;
import com.sparta.team30.products.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductDetailController {
    private final ProductDetailService productDetailService;

    @GetMapping("/{productId}/details")
    public ResponseEntity<ProductDetailResponseDto> getProductDetail(@PathVariable("productId") UUID productId){
        ProductDetailResponseDto response = productDetailService.getProductDetail(productId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/details")
    public ResponseEntity<ProductDetailResponseDto> createProductDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("productId") UUID productId,
                                              @RequestBody ProductDetailRequestDto productDetailRequestDto){
        ProductDetailResponseDto productDetail = productDetailService.createProductDetail(userDetails, productId, productDetailRequestDto);
        return ResponseEntity.ok(productDetail);
    }


}
