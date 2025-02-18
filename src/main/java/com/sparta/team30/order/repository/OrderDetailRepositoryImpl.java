package com.sparta.team30.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.order.domain.QOrder;
import com.sparta.team30.order.domain.QOrderDetail;
import com.sparta.team30.order.dto.ResponseOrderProductDTO;
import com.sparta.team30.products.domain.QProduct;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<ResponseOrderProductDTO> findByOrderId(UUID orderId) {
        QOrder order = QOrder.order;
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;

        List<ResponseOrderProductDTO> productDTOList = queryFactory.select(Projections.constructor(ResponseOrderProductDTO.class,
                        product.productName,
                        orderDetail.count,
                        orderDetail.count.multiply(product.price)
                )).from(orderDetail)
                .leftJoin(orderDetail.product, product)
                .where(orderDetail.order.orderId.eq(orderId))
                .fetch();

        return productDTOList;
    }
}
