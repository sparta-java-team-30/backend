package com.sparta.team30.products.repository;

import com.sparta.team30.products.domain.ProductDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductDetailHistoryRepository extends
        JpaRepository<ProductDetailHistory, UUID>, ProductDetailHistoryRepositoryCustom {
}
