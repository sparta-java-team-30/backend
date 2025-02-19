package com.sparta.team30.carts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartItemRequestDto {
    private UUID cartId;
    private UUID productId;
    private int count;
}
