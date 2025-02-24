package com.sparta.team30.product;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductUpdateTest {
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
    void testUpdateProduct() {
        UUID store_id = UUID.randomUUID();

        // Mock store 생성
        Store mockStore = new Store();
        mockStore.setStoreId(store_id);

        // Given
        UUID productId = UUID.randomUUID();  // 기존 productId
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setProductName("딸기 케이크");
        productRequestDto.setPrice(100);
        productRequestDto.setStoreId(store_id);  // 유효한 storeId 설정

        // storeRepository.findById()가 가게 정보를 반환하도록 설정
        when(storeRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockStore));

        // Mock 기존 상품
        Product existingProduct = new Product();
        existingProduct.setProductId(productId);
        existingProduct.setProductName("초콜릿 케이크");
        existingProduct.setPrice(200);
        existingProduct.setStore(mockStore);

        // Mock productRepository.findById()가 기존 상품을 반환하도록 설정
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Mock productRepository.save()가 업데이트된 상품을 반환하도록 설정
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        ProductResponseDto result = productServiceUnderTest.updateProduct(productId, productRequestDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getProductId());
        assertEquals("딸기 케이크", result.getProductName());  // 업데이트된 제품명 확인
        assertEquals(100, result.getPrice());  // 업데이트된 가격 확인
        assertEquals(mockStore.getStoreId(), result.getStoreId());  // 가게 ID 확인

        // Verify interactions with the repositories
        verify(storeRepository, times(1)).findById(any(UUID.class));  // storeRepository.findById() 호출 확인
        verify(productRepository, times(1)).findById(productId);  // productRepository.findById() 호출 확인
        verify(productRepository, times(1)).save(any(Product.class));  // productRepository.save() 호출 확인

        System.out.println("Result: ");
        System.out.println("Product ID: " + result.getProductId());
        System.out.println("Product Name: " + result.getProductName());
        System.out.println("Price: " + result.getPrice());
        System.out.println("Store ID: " + result.getStoreId());
    }
}
