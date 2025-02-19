package com.sparta.team30.products.service;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductRequestDto;
import com.sparta.team30.products.dto.ProductResponseDto;
import com.sparta.team30.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();

        product.setProductName(productRequestDto.getProductName());
        product.setPrice(productRequestDto.getPrice());

//        Store store = storeRepository.findById(productRequestDto.getStoreId())
//                .orElseThrow(() -> new IllegalArgumentException("가게 아이디를 찾을 수 없습니다. " + productRequestDto.getStoreId()));
//        product.setStore(store);

        Product savedProduct = productRepository.save(product);

        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setProductId(savedProduct.getProductId());
        productResponseDto.setProductName(savedProduct.getProductName());
        productResponseDto.setPrice(savedProduct.getPrice());
        productResponseDto.setStoreId(savedProduct.getStore().getStoreId());

        return productResponseDto;
    }

    public Page<ProductResponseDto> getAllProducts(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        Page<ProductResponseDto> productResponseDtoPage = productPage.map(product -> {
            ProductResponseDto dto = new ProductResponseDto();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setPrice(product.getPrice());
            dto.setStoreId(product.getStore().getStoreId());
            return dto;
        });

        return productResponseDtoPage;
    }

    public ProductResponseDto getProductById(UUID productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product savedProduct = product.get();
            ProductResponseDto productResponseDto = new ProductResponseDto();

            productResponseDto.setProductId(savedProduct.getProductId());
            productResponseDto.setProductName(savedProduct.getProductName());
            productResponseDto.setPrice(savedProduct.getPrice());
            productResponseDto.setStoreId(savedProduct.getStore().getStoreId());

            return productResponseDto;
        }

        return null;
    }

    public ProductResponseDto updateProduct(UUID productId, ProductRequestDto productRequestDto) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setProductName(productRequestDto.getProductName());
            product.setPrice(productRequestDto.getPrice());

            Product updatedProduct = productRepository.save(product);

            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductId(updatedProduct.getProductId());
            productResponseDto.setProductName(updatedProduct.getProductName());
            productResponseDto.setPrice(updatedProduct.getPrice());
            productResponseDto.setStoreId(updatedProduct.getStore().getStoreId());

            return productResponseDto;
        } else {
            return null;
        }
    }

    public boolean deleteProduct(UUID productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        } else {
            return false;
        }
    }
}
