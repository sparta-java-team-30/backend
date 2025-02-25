package com.sparta.team30.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.team30.address.domain.QAddress;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderDetail;
import com.sparta.team30.order.domain.QOrder;
import com.sparta.team30.order.domain.QOrderDetail;
import com.sparta.team30.order.dto.ResponseMyStoreOrderListDTO;
import com.sparta.team30.order.dto.ResponseOrderProductDTO;
import com.sparta.team30.products.domain.QProduct;
import com.sparta.team30.store.domain.QStore;
import com.sparta.team30.user.domain.QUser;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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
                .where(
                        orderDetail.order.orderId.eq(orderId),
                        orderDetail.isDeleted.eq(false)
                        )
                .fetch();

        return productDTOList;
    }

    @Override
    public String findStoreNameByOrderId(UUID orderId) {
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QStore store = QStore.store;
        QProduct product = QProduct.product;
        String s = queryFactory.select(
                        store.storeName
                ).from(orderDetail)
                .leftJoin(orderDetail.product, product)
                .leftJoin(product.store, store)
                .where(orderDetail.order.orderId.eq(orderId))
                .fetchOne();
        return s;
    }

    @Override
    public List<ResponseMyStoreOrderListDTO> getMyStoreOrderList(UUID storeId) {
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        QProduct product = QProduct.product;
        QOrder order = QOrder.order;
        QUser user = QUser.user;
        QAddress address = QAddress.address;

        List<OrderDetail> orderDetailList = queryFactory
                .selectFrom(orderDetail)
                .join(orderDetail.product, product)
                .join(orderDetail.order, order)
                .join(order.user, user)
                .join(order.address, address)
                .where(product.store.storeId.eq(storeId))
                .fetch();

        List<ResponseMyStoreOrderListDTO> myStoreOrderList = new ArrayList<>();

        for(OrderDetail orderDetail1 : orderDetailList) {
            Order order1 = orderDetail1.getOrder();

            List<ResponseOrderProductDTO> orderProductDTOList = new ArrayList<>();
            for(OrderDetail orderDetail2 : order1.getOrderDetails()) {
                orderProductDTOList.add(
                        new ResponseOrderProductDTO(
                                orderDetail2.getProduct().getProductName(),
                                orderDetail2.getCount(),
                                orderDetail2.getProduct().getPrice()
                        ));
            }

            ResponseMyStoreOrderListDTO MyStoreOrderListDTO = new ResponseMyStoreOrderListDTO(
                    order1.getOrderId(),
                    order1.getOrderType(),
                    order1.getOrderStatus(),
                    order1.getPrice(),
                    order1.getCreatedAt(),
                    orderProductDTOList,
                    order1.getAddress().getUserPostcode() +
                            order1.getAddress().getUserAddress1() +
                            order1.getAddress().getUserAddress2(),
                    order1.getUser().getUsername()
            );

            myStoreOrderList.add(MyStoreOrderListDTO);
        }

        return myStoreOrderList;
    }

}
