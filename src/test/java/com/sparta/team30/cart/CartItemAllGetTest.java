package com.sparta.team30.cart;

import com.sparta.team30.carts.domain.Cart;
import com.sparta.team30.carts.domain.CartItem;
import com.sparta.team30.carts.dto.CartItemResponseDto;
import com.sparta.team30.carts.repository.CartItemRepository;
import com.sparta.team30.carts.repository.CartRepository;
import com.sparta.team30.carts.service.CartService;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartItemAllGetTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private Long userId;
    private User mockUser;
    private Cart mockCart;
    private CartItem mockCartItem;
    private Product mockProduct;
    private UUID cartId;
    private UUID cartItemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCartItem() {
        userId = 1L;
        mockUser = mock(User.class);
        mockCart = new Cart();
        mockProduct = mock(Product.class);
        cartItemId = UUID.randomUUID();
        cartId = UUID.randomUUID();

        mockCart.setCartId(cartId);
        mockCart.setUser(mockUser);

        mockCartItem = new CartItem();
        mockCartItem.setCart(mockCart);
        mockCartItem.setProduct(mockProduct);
        mockCartItem.setCartItemId(cartItemId);
        mockCartItem.setCount(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));

        List<CartItem> cartItemList = List.of(mockCartItem);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "cartItemId"));
        Page<CartItem> cartItemPage = new PageImpl<>(cartItemList, pageable, cartItemList.size());

        when(cartItemRepository.findByCart_CartId(cartId, pageable)).thenReturn(cartItemPage);

        int page = 0;
        int size = 5;
        String sortBy = "cartItemId";
        boolean isAsc = true;

        Page<CartItemResponseDto> result = cartService.getCartItems(userId, cartId, page, size, sortBy, isAsc);

        assertNotNull(result);
        assertThat(result.getTotalElements()).isEqualTo(cartItemPage.getTotalElements());
        assertThat(result.getContent().get(0).getCartItemId()).isEqualTo(mockCartItem.getCartItemId());
        assertThat(result.getContent().get(0).getCount()).isEqualTo(mockCartItem.getCount());
        assertThat(result.getContent().get(0).getProductName()).isEqualTo(mockProduct.getProductName());

        verify(cartItemRepository, times(1)).findByCart_CartId(cartId, pageable);
    }
}