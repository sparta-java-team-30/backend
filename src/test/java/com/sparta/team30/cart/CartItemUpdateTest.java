package com.sparta.team30.cart;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.dto.CartItemResponseDto;
import com.sparta.team30.carts.repository.CartItemRepository;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.carts.service.CartService;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
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

public class CartItemUpdateTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private Long userId;
    private User mockUser;
    private Cart mockCart;
    private CartItem mockCartItem;
    private Product mockProduct;
    private UUID cartItemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = 1L;
        mockUser = mock(User.class);
        mockCart = new Cart();
        mockProduct = mock(Product.class);
        cartItemId = UUID.randomUUID();

        mockCart.setCartId(UUID.randomUUID());
        mockCart.setUser(mockUser);

        mockCartItem = new CartItem();
        mockCartItem.setCart(mockCart);
        mockCartItem.setProduct(mockProduct);
        mockCartItem.setCartItemId(cartItemId);
        mockCartItem.setCount(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(mockCartItem));
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockProduct));
    }

    @Test
    void testUpdateCartItem() {
        int newCount = 5;

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));

        mockCart.getCartItems().add(mockCartItem);

        CartItemResponseDto updatedCartItem = cartService.updateCartItem(userId, cartItemId, newCount);

        assertNotNull(updatedCartItem);
        assertEquals(newCount, updatedCartItem.getCount());

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }
}