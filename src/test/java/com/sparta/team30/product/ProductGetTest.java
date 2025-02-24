package com.sparta.team30.product;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductResponseDto;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.products.service.ProductService;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductGetTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productServiceUnderTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProduct() {
        UUID storeId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Mock Store 생성
        Store mockStore = new Store();
        mockStore.setStoreId(storeId);

        // Mock Product 생성
        Product mockProduct = new Product();
        mockProduct.setProductId(productId);
        mockProduct.setProductName("딸기 케이크");
        mockProduct.setPrice(100);
        mockProduct.setStore(mockStore);

        // Mock 동작 정의
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // 실행
        ProductResponseDto result = productServiceUnderTest.getProductById(productId);

        // 검증
        assertNotNull(result);
        assertEquals(mockProduct.getProductId(), result.getProductId());
        assertEquals(mockProduct.getProductName(), result.getProductName());
        assertEquals(mockProduct.getPrice(), result.getPrice());

        // productRepository.findById가 한 번 호출되었는지 검증
        verify(productRepository, times(1)).findById(productId);

        System.out.println("Result: ");
        System.out.println("Product ID: " + result.getProductId());
        System.out.println("Product Name: " + result.getProductName());
    }
}