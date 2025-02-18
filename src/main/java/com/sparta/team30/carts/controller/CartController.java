package com.sparta.team30.carts.controller;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

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
