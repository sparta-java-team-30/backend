package com.sparta.team30.carts.service;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.dto.CartItemRequestDto;
import com.sparta.team30.carts.dto.CartItemResponseDto;
import com.sparta.team30.carts.dto.CartResponseDto;
import com.sparta.team30.carts.repository.CartItemRepository;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UUID createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Cart cart = new Cart();
        cart.setUser(user);
        Cart savedCart = cartRepository.save(cart);

        return savedCart.getCartId();
    }

    public UUID getCartId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(null);

        return (cart != null) ? cart.getCartId() : null;
    }

    public CartItemResponseDto addItemToCart(Long userId, CartItemRequestDto cartItemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(cartItemRequestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCount(cartItemRequestDto.getCount());

        cartItemRepository.save(cartItem);

        return new CartItemResponseDto(cartItem);
    }

    public CartItemResponseDto updateCartItem(Long userId, UUID cartItemId, int count) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다."));

        cartItem.setCount(count);
        cartItemRepository.save(cartItem);

        return new CartItemResponseDto(cartItem);
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

    public Page<CartItemResponseDto> getCartItems(Long userId, UUID cartId, int page, int size, String sortBy, boolean isAsc) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));

        Page<CartItem> cartItemsPage = Optional.ofNullable(cartItemRepository.findByCart_CartId(cart.getCartId(), pageable))
                .orElse(Page.empty());

        System.out.println("cartItemsPage size: " + cartItemsPage.getTotalElements());

        List<CartItemResponseDto> cartItemResponseDtos = cartItemsPage.getContent().stream()
                .map(CartItemResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(cartItemResponseDtos, pageable, cartItemsPage.getTotalElements());
    }
}
