package com.sparta.team30.carts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDto {
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}
