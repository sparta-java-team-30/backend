package com.sparta.team30.products.repository;

import com.sparta.team30.products.domain.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID>, ProductDetailRepositoryCustom {
}
