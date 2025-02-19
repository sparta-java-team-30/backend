package com.sparta.team30.products.repository;

import com.sparta.team30.products.domain.ProductDetail;

import java.util.UUID;

public interface ProductDetailRepositoryCustom {
    ProductDetail findByProductId(UUID productId);
}
