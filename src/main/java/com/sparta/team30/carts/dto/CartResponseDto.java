package com.sparta.team30.carts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDto {

    @Schema(description = "장바구니 아이디", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID cartId;

    private Long userId;

    @Schema(description = "장바구니 아이템 목록")
    private List<CartItemResponseDto> cartItems;
}
