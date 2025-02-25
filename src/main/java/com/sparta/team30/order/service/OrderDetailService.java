package com.sparta.team30.order.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderDetail;
import com.sparta.team30.order.dto.ResponseMyStoreOrderListDTO;
import com.sparta.team30.order.dto.ResponseOrderProductDTO;
import com.sparta.team30.order.repository.OrderDetailRepository;
import com.sparta.team30.products.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;


    public void addOrderProducts(Order order, List<Product> productList) {

        List<OrderDetail> orderDetailList = productList.stream().map(product ->
                new OrderDetail(order, product)).toList();

        orderDetailRepository.saveAll(orderDetailList);
    }

    public List<ResponseOrderProductDTO> getOrderProductList(UUID orderId) {

        return orderDetailRepository.findByOrderId(orderId);
    }

    public String getStoreName(UUID orderId) {
        return orderDetailRepository.findStoreNameByOrderId(orderId);
    }

    public List<ResponseMyStoreOrderListDTO> getMyStoreOrderList(UUID storeId) {
        return orderDetailRepository.getMyStoreOrderList(storeId);
    }
}
