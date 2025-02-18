package com.sparta.team30.products.service;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductDto;
import com.sparta.team30.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        //product.setStore(store);

        return productRepository.save(product);
    }
}
