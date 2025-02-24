package com.sparta.team30.cart;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.carts.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CartItemDeleteTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private Cart mockCart;
    private CartItem mockCartItem;
    private Long userId = 1L;
    private UUID cartItemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCart = new Cart();
        mockCartItem = new CartItem();
        cartItemId = UUID.randomUUID();

        mockCartItem.setCartItemId(cartItemId);
        mockCart.getCartItems().add(mockCartItem);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));
    }

    @Test
    void testDeleteCartItem_success() {
        cartService.deleteCartItem(userId, cartItemId);

        verify(cartRepository, times(1)).save(mockCart);

        assertTrue(mockCart.getCartItems().isEmpty());
    }

    @Test
    void testDeleteCartItem_itemNotFound() {
        UUID nonExistingCartItemId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartItem(userId, nonExistingCartItemId);
        });

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testDeleteCartItem_cartNotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartItem(userId, cartItemId);
        });

        verify(cartRepository, never()).save(any(Cart.class));
    }
}