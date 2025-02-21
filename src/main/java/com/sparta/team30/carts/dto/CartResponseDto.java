package com.sparta.team30.carts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDto {
    private UUID cartId;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}
