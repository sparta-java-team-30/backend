package com.sparta.team30.carts.service;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.dto.CartItemRequestDto;
import com.sparta.team30.carts.dto.CartItemResponseDto;
import com.sparta.team30.carts.repository.CartItemRepository;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemResponseDto addItemToCart(Long userId, CartItemRequestDto cartItemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<Product> product = productRepository.findById(cartItemRequestDto.getProductId());

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product.get());
        cartItem.setCount(cartItemRequestDto.getCount());

        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        return cartItemResponseDto;
    }

    public CartItemResponseDto updateCartItem(Long userId, UUID cartItemId, int count) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다."));

        cart.getCartItems().add(cartItem);

        cartItemRepository.save(cartItem);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        return cartItemResponseDto;
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

    public UUID getCartId(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        return cart.map(Cart::getCartId).orElse(null);
    }

    public UUID createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Cart cart = new Cart();
        cart.setUser(user);

        cartRepository.save(cart);
        return cart.getCartId();
    }

    public Page<CartItemResponseDto> getCartItems(UUID cartId, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CartItem> cartItems = cartItemRepository.findByCart_CartId(cartId, pageable);

        return cartItems.map(cartItem -> new CartItemResponseDto(
        ));
    }
}
