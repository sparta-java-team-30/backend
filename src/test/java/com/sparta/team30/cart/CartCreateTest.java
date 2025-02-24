package com.sparta.team30.cart;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.repository.CartItemRepository;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.carts.service.CartService;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartCreateTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        Long userId = 1L;
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        Cart mockCart = new Cart();
        UUID cartId = UUID.randomUUID();
        mockCart.setCartId(cartId);
        mockCart.setUser(mockUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        UUID createdCartId = cartService.createCart(userId);

        System.out.println("User Id: " + userId);
        System.out.println("Created Cart ID: " + createdCartId);

        assertNotNull(createdCartId);
        assertEquals(cartId, createdCartId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}