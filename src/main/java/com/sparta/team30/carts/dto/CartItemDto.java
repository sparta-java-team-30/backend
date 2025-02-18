package com.sparta.team30.carts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CartItemDto {
    private UUID cartId;
    private UUID productId;
    private int count;
}
