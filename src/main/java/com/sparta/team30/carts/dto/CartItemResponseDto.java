package com.sparta.team30.carts.dto;

import com.sparta.team30.carts.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CartItemResponseDto {

    @Schema(description = "장바구니 아이템 아이디", example = "e8b9c72d-9e7b-4a7f-bd2c-7d8fbd7d12f5")
    private UUID cartItemId;

    @Schema(description = "장바구니 아이디", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID cartId;

    @Schema(description = "상품 아이디", example = "b6b3c72d-fbbb-4a47-9b49-8fa67c24fef9")
    private UUID productId;

    private String productName;

    @Schema(description = "상품 수량", example = "2")
    private int count;

    public static CartItemResponseDto from(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setCartItemId(cartItem.getCartItemId());
        dto.setProductId(cartItem.getProduct().getProductId());
        dto.setProductName(cartItem.getProduct().getProductName());
        dto.setCount(cartItem.getCount());
        return dto;
    }
}