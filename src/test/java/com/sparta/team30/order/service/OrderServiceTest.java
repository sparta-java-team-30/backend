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
import com.sparta.team30.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("주문 생성 테스트")
    class AddOrder{
        @Test
        void testAddOrderSuccess() {

            // given
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            List<RequestOrderProductDTO> productDTOList = List.of(
                    new RequestOrderProductDTO(UUID.randomUUID(), "햄버거", 3, 2000),
                    new RequestOrderProductDTO(UUID.randomUUID(), "밥", 2, 2000)
            );
            requestCreateOrderDTO.setProductList(productDTOList);
            List<UUID> productIdList = List.of(UUID.randomUUID(),UUID.randomUUID());
            User user = User.builder().username("test").build();
            Order mockOrder = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY, 10000, user);
            Product mockProduct = new Product();
            List<Product> mockProductList = List.of(mockProduct);

            when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
            when(productRepository.findAllById(anyList())).thenReturn(mockProductList);
            doNothing().when(orderDetailService).addOrderProducts(any(Order.class), anyList());

            //when
            ResponseCreateOrderDTO responseCreateOrderDTO = orderService.addOrder(user.getUsername(),requestCreateOrderDTO);

            //then
            assertEquals("주문이 완료되었습니다.", responseCreateOrderDTO.getMessage());
        }

        @Test
        void testAddOrderFail() {
            // given
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            List<RequestOrderProductDTO> productList = new ArrayList<>();

            requestCreateOrderDTO.setProductList(productList);
            User user = User.builder().username("test").build();
            //when-then
            Order mockOrder = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY,0, user);

            assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(user.getUsername(),requestCreateOrderDTO));
        }
    }

    @Nested
    @DisplayName("사용자 주문 내역 조회  테스트")
    class GetOrder {
        @Test
        void testGetOrderListSuccess() {


        }
    }

}