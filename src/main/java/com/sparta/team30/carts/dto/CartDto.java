package com.sparta.team30.carts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartDto {
    private Long userId;
    private List<CartItemDto> cartItems;
}
