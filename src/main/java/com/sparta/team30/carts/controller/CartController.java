package com.sparta.team30.carts.controller;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<CartItem>> getCartItems(
            @PathVariable Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {
        UUID cartId = cartService.getCartId(userId);

        if (cartId == null) {
            cartId = cartService.createCart(userId);
        }

        Page<CartItem> cartItems = cartService.getCartItems(cartId, page - 1, size, sortBy, isAsc);

        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        Cart updatedCart = cartService.addItemToCart(userId, cartItem);
        return ResponseEntity.ok(updatedCart);
    }

    @PatchMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long userId,
                                               @PathVariable UUID cartItemId,
                                               @RequestParam int count) {
        Cart updatedCart = cartService.updateCartItem(userId, cartItemId, count);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long userId, @PathVariable UUID cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }
}
