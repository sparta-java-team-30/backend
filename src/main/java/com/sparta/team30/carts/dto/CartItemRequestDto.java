package com.sparta.team30.carts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartItemRequestDto {

    @Schema(description = "장바구니 아이디", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID cartId;

    @Schema(description = "상품 아이디", example = "b6b3c72d-fbbb-4a47-9b49-8fa67c24fef9")
    private UUID productId;

    @Schema(description = "상품 수량", example = "2")
    private int count;
}