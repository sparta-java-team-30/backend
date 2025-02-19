package com.sparta.team30.products.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductDetailHistoryRepositoryImpl implements ProductDetailHistoryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
}
