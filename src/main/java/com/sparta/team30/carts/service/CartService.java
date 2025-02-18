package com.sparta.team30.carts.service;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.repository.CartItemRepository;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public Cart addItemToCart(Long userId, CartItem cartItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(Long userId, UUID cartItemId, int count) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다."));

        cartItem.setCount(count);

        return cartRepository.save(cart);
    }

    public void deleteCartItem(Long userId, UUID cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        boolean removed = cart.getCartItems().removeIf(item -> item.getCartItemId().equals(cartItemId));

        if (!removed) {
            throw new IllegalArgumentException("해당 상품이 장바구니에 없습니다.");
        }

        cartRepository.save(cart);
    }
}
