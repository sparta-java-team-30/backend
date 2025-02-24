package com.sparta.team30.product;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductResponseDto;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.products.service.ProductService;
import com.sparta.team30.store.domain.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductDeleteTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productServiceUnderTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteProduct() {
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
        boolean result = productServiceUnderTest.deleteProduct(productId);

        // 검증
        assertTrue(result);

        // productRepository.findById가 한 번 호출되었는지 검증
        verify(productRepository, times(1)).findById(productId);

        System.out.println("Result: " + result);
    }
}
