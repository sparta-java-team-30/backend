package com.sparta.team30.carts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartRequestDto {
    private Long userId;

    @Schema(description = "장바구니 아이템 목록")
    private List<CartItemResponseDto> cartItems;
}
