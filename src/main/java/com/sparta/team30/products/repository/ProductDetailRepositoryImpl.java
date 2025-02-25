package com.sparta.team30.products.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.products.domain.ProductDetail;
import com.sparta.team30.products.domain.QProductDetail;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductDetailRepositoryImpl implements ProductDetailRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ProductDetail findByProductId(UUID productId) {
        return jpaQueryFactory
                .selectFrom(QProductDetail.productDetail)
                .where(QProductDetail.productDetail.product.productId.eq(productId))
                .where(QProductDetail.productDetail.isDeleted.eq(false))
                .fetchOne();
    }
}
