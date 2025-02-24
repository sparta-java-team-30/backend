package com.sparta.team30.product;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductRequestDto;
import com.sparta.team30.products.dto.ProductResponseDto;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.products.service.ProductService;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

class ProductCreateTest {
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productServiceUnderTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 정상 작동
    @Test
    void testCreateProduct() {
        // Given
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setProductName("초콜릿 케이크");
        productRequestDto.setPrice(200);
        productRequestDto.setStoreId(UUID.randomUUID());

        Store mockStore = new Store();
        mockStore.setStoreId(productRequestDto.getStoreId());
        when(storeRepository.findById(productRequestDto.getStoreId())).thenReturn(Optional.of(mockStore));

        Product mockProduct = new Product();
        mockProduct.setProductId(UUID.randomUUID()); // productId 추가
        mockProduct.setProductName("초콜릿 케이크");
        mockProduct.setPrice(200);
        mockProduct.setStore(mockStore);

        // Mocking productRepository.save() to return a saved product
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        // When
        ProductResponseDto result = productServiceUnderTest.createProduct(productRequestDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getProductId());
        assertEquals("초콜릿 케이크", result.getProductName());
        assertEquals(200, result.getPrice());
        assertEquals(mockStore.getStoreId(), result.getStoreId());

        verify(storeRepository, times(1)).findById(productRequestDto.getStoreId());
        verify(productRepository, times(1)).save(any(Product.class));

        System.out.println("Result: ");
        System.out.println("Product ID: " + result.getProductId());
        System.out.println("Product Name: " + result.getProductName());
        System.out.println("Price: " + result.getPrice());
        System.out.println("Store ID: " + result.getStoreId());
    }
}