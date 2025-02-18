package com.sparta.team30.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.order.domain.QOrder;
import com.sparta.team30.order.domain.QOrderDetail;
import com.sparta.team30.order.dto.ResponseOrderHistoryDTO;
import com.sparta.team30.products.domain.QProduct;
import com.sparta.team30.store.domain.QStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QOrder order = QOrder.order;
    QOrderDetail orderDetail = QOrderDetail.orderDetail;
    QProduct product = QProduct.product;
    QStore store = QStore.store;

    public Page<ResponseOrderHistoryDTO> findByUserIdAndProductOrStoreName(String search, Long userId, Pageable pageable ,boolean isAsc) {
        List<ResponseOrderHistoryDTO> response = jpaQueryFactory.select(Projections.constructor(ResponseOrderHistoryDTO.class,
                        order.orderId,
                        order.user.id,
                        order.orderType,
                        order.updatedAt,
                        order.orderStatus,
                        order.price
                        //  product.store.storeName
                ))
                .from(order)
                .leftJoin(order.orderDetails, orderDetail)
                .leftJoin(orderDetail.product, product)
                //  .leftJoin(product.store,store)
                .where(
                        order.user.id.eq(userId),
                        order.isDeleted.eq(false),
                        containsProductName(search)
                        //    ,containsStoreName(search)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(isAsc ? order.updatedAt.desc() : order.updatedAt.asc())
                .fetch();

        JPAQuery<Long> query = getTotalCount(userId, search);

        return PageableExecutionUtils.getPage(response, pageable, () -> query.fetchOne());
    }

    JPAQuery<Long> getTotalCount(Long userId, String search) {
        return jpaQueryFactory
                .select(order.count())  // 👉 전체 개수 조회
                .from(order)
                .leftJoin(order.orderDetails, orderDetail)
                .leftJoin(orderDetail.product, product)
                //  .leftJoin(product.store, store)
                .where(
                        order.user.id.eq(userId),
                        containsProductName(search)
                        //            ,containsStoreName(search)
                );
    }
    private BooleanExpression containsStoreName(String storeName) {
        return (storeName != null && !storeName.isEmpty()) ? QStore.store.storeName.contains(storeName) : null;
    }

    private BooleanExpression containsProductName(String productName) {
        return (productName != null && !productName.isEmpty()) ? QProduct.product.productName.contains(productName) : null;
    }


}
