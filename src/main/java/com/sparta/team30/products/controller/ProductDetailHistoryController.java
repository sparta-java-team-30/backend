package com.sparta.team30.products.controller;

import com.sparta.team30.products.dto.ProductDetailHistoryResponseDto;
import com.sparta.team30.products.service.ProductDetailHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductDetailHistoryController {
    private final ProductDetailHistoryServiceImpl productDetailHistoryService;

    @PostMapping("/{productId}/recommend")
    public ResponseEntity<ProductDetailHistoryResponseDto> geminiTest(@PathVariable("productId") UUID productId){
        ProductDetailHistoryResponseDto productDetailHistoryResponse =
                productDetailHistoryService.generateContent(productId);

        return ResponseEntity.ok(productDetailHistoryResponse);
    }
}
