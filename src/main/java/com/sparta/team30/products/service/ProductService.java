package com.sparta.team30.products.service;

import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.dto.ProductDto;
import com.sparta.team30.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.orElse(null);
    }

    public Product updateProduct(UUID productId, ProductDto productDto) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setProductName(productDto.getProductName());
            product.setPrice(productDto.getPrice());
            return productRepository.save(product);
        } else {
            return null;
        }
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
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
