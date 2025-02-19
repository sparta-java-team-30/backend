package com.sparta.team30.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductResponseDto {
    @Schema(description = "상품 아이디", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID productId;

    @Schema(description = "상품 이름", example = "초콜릿 케이크")
    private String productName;

    @Schema(description = "상품 가격", example = "12000")
    private int price;

    @Schema(description = "가게 아이디", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID storeId;
}
