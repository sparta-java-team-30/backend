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
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductAllGetTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productServiceUnderTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllGetProduct() {
        // given
        UUID storeId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Store mockStore = new Store();
        mockStore.setStoreId(storeId);

        Product mockProduct = new Product();
        mockProduct.setProductId(productId);
        mockProduct.setProductName("딸기 케이크");
        mockProduct.setPrice(100);
        mockProduct.setStore(mockStore);

        List<Product> productList = List.of(mockProduct);
        Pageable pageable = PageRequest.of(0, 10);  // 첫 번째 페이지, 10개씩 조회
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        // when(productRepository.findAll())가 아닌 findAll(Pageable) 사용
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        // when - getAllProducts 호출 (페이지 번호: 0, 사이즈: 10, 정렬 기준: productName, 오름차순)
        Page<ProductResponseDto> result = productServiceUnderTest.getAllProducts(0, 10, "productName", true);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mockProduct.getProductId(), result.getContent().get(0).getProductId());
        assertEquals(mockProduct.getProductName(), result.getContent().get(0).getProductName());

        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }
}