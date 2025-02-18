package com.sparta.team30.order.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderDetail;
import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.dto.RequestOrderProductDTO;
import com.sparta.team30.order.dto.ResponseCreateOrderDTO;
import com.sparta.team30.order.repository.OrderDetailRepository;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Test
    @DisplayName("주문 생성 성공 테스트")
    void testAddOrderSuccess() {
        // given
        RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
        List<RequestOrderProductDTO> productList = List.of(
                new RequestOrderProductDTO(UUID.randomUUID(), "햄버거", 3, 2000),
                new RequestOrderProductDTO(UUID.randomUUID(), "밥", 2, 2000)
        );
        requestCreateOrderDTO.setProductList(productList);

        Order mockOrder = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        doNothing().when(orderDetailService).addOrderProducts(any(Order.class), anyList());
        ResponseCreateOrderDTO responseCreateOrderDTO = orderService.addOrder(requestCreateOrderDTO);

        assertEquals("주문이 완료되었습니다.", responseCreateOrderDTO.getMessage());
    }

    @Test
    @DisplayName("주문 생성 실패 테스트")
    void testAddOrderFail() {
        // given
        RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
        List<RequestOrderProductDTO> productList = new ArrayList<>();

        requestCreateOrderDTO.setProductList(productList);
        Order mockOrder = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY);

        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(requestCreateOrderDTO));
    }
}