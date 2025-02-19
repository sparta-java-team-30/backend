package com.sparta.team30.products.controller;

import com.sparta.team30.products.dto.ProductDetailRequestDto;
import com.sparta.team30.products.dto.ProductDetailResponseDto;
import com.sparta.team30.products.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductDetailResponseDto> createProductDetail(@PathVariable("productId") UUID productId,
                                              @RequestBody ProductDetailRequestDto productDetailRequestDto){
        ProductDetailResponseDto productDetail = productDetailService.createProductDetail(productId, productDetailRequestDto);
        return ResponseEntity.ok(productDetail);
    }


}
