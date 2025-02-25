package com.sparta.team30.order.service;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.dto.RequestCreateAddressDTO;
import com.sparta.team30.address.repository.AddressRepository;
import com.sparta.team30.common.exception.OrderAccessDeniedException;
import com.sparta.team30.common.exception.OrderAlreadyProcessedException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.order.dto.*;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import com.sparta.team30.payment.service.PaymentOfOrderService;
import com.sparta.team30.payment.service.PaymentService;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

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

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PaymentOfOrderService paymentofOrderService;



    @Nested
    @DisplayName("주문 생성 테스트")
    class AddOrder{
        @Test
        @DisplayName("주문 성공 테스트")
        void testAddOrderSuccess() {

            // given
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            //요청 생성
            UUID productId1 = UUID.randomUUID();
            List<RequestOrderProductDTO> productDTOList = List.of(
                    new RequestOrderProductDTO(productId1, "햄버거", 3)
            );
            requestCreateOrderDTO.setProductList(productDTOList);

            //가짜 객체 생성
            User mockuser = User.builder().username("test").role(UserRoleEnum.USER).build();
            User mockOWNERuser = User.builder().username("owner").role(UserRoleEnum.OWNER).build();
            Address mockAddress = new Address(mockuser,new RequestCreateAddressDTO("12345","서울시 광화문","ㅁㅁ동 ㅁㅁ호"));
            Product mockProduct = new Product();
            mockProduct.setProductId(productId1);
            mockProduct.setPrice(1000);
            List<Product> mockProductList = List.of(mockProduct);
            Order mockOrder = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY, mockProduct.getPrice()*3, mockuser, mockAddress);
            //임의 응답 설정
            when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
            when(productRepository.findAllById(anyList())).thenReturn(mockProductList);
            when(addressRepository.findByUsernameAndAddressIsDefault(any(String.class))).thenReturn(mockAddress);
            doNothing().when(orderDetailService).addOrderProducts(any(Order.class), anyList());
            when(userRepository.findByUsername("test")).thenReturn(Optional.of(mockuser));
            when(userRepository.findByUsername("owner")).thenReturn(Optional.of(mockOWNERuser));
            when(productRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(mockProduct));

            //when
            ResponseCreateOrderDTO responseDeliveryOrder = orderService.addOrder(mockuser.getUsername(),requestCreateOrderDTO);
            ResponseCreateOrderDTO responsePickupOrder = orderService.addOrder(mockOWNERuser.getUsername(),requestCreateOrderDTO);
            //then
            assertEquals("주문이 완료되었습니다.", responseDeliveryOrder.getMessage());
            assertEquals(OrderTypeEnum.DELIVERY,responseDeliveryOrder.getOrderType());
            assertEquals(3000, mockOrder.getPrice());

            assertEquals("주문이 완료되었습니다.", responseDeliveryOrder.getMessage());
            assertEquals(OrderTypeEnum.PICKUP,responsePickupOrder.getOrderType());
            assertEquals(3000, mockOrder.getPrice());
            //assertEquals(any(ResponseCreateOrderDTO.class), orderService.addOrder(user.getUsername(), requestCreateOrderDTO));
        }

        @Test
        @DisplayName("주문 실패 테스트(선택 상품 존재 x)")
        void testAddOrderFailCauseEmptyProducts() {
            // given
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            List<RequestOrderProductDTO> productList = new ArrayList<>();
            requestCreateOrderDTO.setProductList(productList);
            //가짜 객체 생성
            User mockuser = User.builder().username("test").role(UserRoleEnum.USER).build();
            Address mockAddress = new Address(mockuser,new RequestCreateAddressDTO("12345","서울시 광화문","ㅁㅁ동 ㅁㅁ호"));
            Product mockProduct = new Product();
            Order mockOrder = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY, mockProduct.getPrice()*3, mockuser, mockAddress);
            //임의 응답 설정
            when(addressRepository.findByUsernameAndAddressIsDefault(any(String.class))).thenReturn(mockAddress);
            when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(mockuser));

            //when-then
            assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(mockuser.getUsername(),requestCreateOrderDTO));
        }
    }

    @Nested
    @DisplayName("주문 조회 테스트")
    class GetOrder {
        @Test
        @DisplayName(("주문 내역 조회 테스트"))
        void testGetOrderListSuccess() {

            //given
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            User owner = User.builder().username("owner").role(UserRoleEnum.OWNER).build();
            User master = User.builder().username("master").role(UserRoleEnum.MASTER).build();
            UserDetails userDetails = mock(UserDetails.class);
            Address mockAddress = new Address();
            List<RequestOrderProductDTO> productDTOList = List.of(
                    new RequestOrderProductDTO(new Product().getProductId(), "햄버거", 3)
                    //new RequestOrderProductDTO(productId2, "밥", 2)
            );
            requestCreateOrderDTO.setComment("주문 1");
            Order order1 = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY, 0, user, mockAddress);
            Order order2 = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY, 0, user, mockAddress);
            Page<ResponseOrderHistoryDTO> mockPage = new PageImpl(List.of(order1, order2));
            Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
            when(userDetails.getUsername()).thenReturn("user");
            when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
            when(orderRepository.findByUserIdAndProductOrStoreName
                    (nullable(String.class), nullable(Long.class), any(UserRoleEnum.class), any(String.class), any(Pageable.class), any(Boolean.class)))
                    .thenReturn(mockPage);

            //when
            long orderListCount = orderService.getOrderHistory(userDetails, "a", 0, 10, true).getTotalElements();

            //then
            assertEquals(2, orderListCount);
        }

        @Test
        @DisplayName("주문 상세 조회 테스트")
        void testGetOrderDetailSuccess() {

            //given
            User mockuser = User.builder().username("user").role(UserRoleEnum.USER).build();
            Address mockAddress = new Address(mockuser, new RequestCreateAddressDTO("12345", "서울시 광화문", "ㅁㅁ동 ㅁㅁ호"));
            UUID orderId = UUID.randomUUID();
            ResponseOrderProductDTO mockProductdto1 = new ResponseOrderProductDTO("상품 1", 2, 2000);
            ResponseOrderProductDTO mockProductdto2 = new ResponseOrderProductDTO("상품 2", 3, 1000);
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order mockOrder = new Order(requestCreateOrderDTO, null, 7000, mockuser, mockAddress);
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn("user");
            when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
            when(orderDetailService.getOrderProductList(any(UUID.class))).thenReturn(List.of(mockProductdto1, mockProductdto2));
            when(orderDetailService.getStoreName(any())).thenReturn("음식점");

            //when
            ResponseOrderDetailsDTO orderDetails = orderService.getOrderDetails(userDetails, orderId);

            //then
            assertEquals(7000, orderDetails.getPrice());
            assertEquals(2, orderDetails.getProductList().size());
        }
    }
    @Nested
    @DisplayName("주문 상태변경 테스트")
    class UpdateDeleteOrder {
        @Test
        @DisplayName("주문 상태변경 실패 테스트(5분 초과)")
        void testUpdateOrderFailed1() {

            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            UserDetails userDetails = mock(UserDetails.class);

            Address mockAddress = new Address(user, new RequestCreateAddressDTO("12345", "서울시 광화문", "ㅁㅁ동 ㅁㅁ호"));
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order mockOrder = spy(new Order(requestCreateOrderDTO, null, 7000, user, mockAddress));

            when(userDetails.getUsername()).thenReturn("user");
            when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
            when(mockOrder.getUpdatedAt()).thenReturn(LocalDateTime.now().minusMinutes(10));
            RequestUpdateOrderDTO updateDTO = new RequestUpdateOrderDTO();

            //when-then
            assertThrows(OrderAlreadyProcessedException.class, () -> orderService.updateOrder(userDetails, UUID.randomUUID(), updateDTO));


        }

        @Test
        @DisplayName("주문 상태변경 테스트(사용자 다름)")
        void testUpdateOrderFailed2() {

            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            User user2 = User.builder().username("anotheruser").role(UserRoleEnum.USER).build();
            UserDetails userDetails = mock(UserDetails.class);

            Address mockAddress = new Address(user, new RequestCreateAddressDTO("12345", "서울시 광화문", "ㅁㅁ동 ㅁㅁ호"));
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order mockOrder = spy(new Order(requestCreateOrderDTO, null, 7000, user, mockAddress));

            when(userDetails.getUsername()).thenReturn("user");
            when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
            when(mockOrder.getUser()).thenReturn(user2);
            RequestUpdateOrderDTO updateDTO = new RequestUpdateOrderDTO();

            //when-then
            assertThrows(OrderAccessDeniedException.class, () -> orderService.updateOrder(userDetails, UUID.randomUUID(), updateDTO));


        }

        @Test
        @DisplayName("주문 상태변경 테스트(결제 완료)")
        void testUpdateOrderFailed3() {

            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            UserDetails userDetails = mock(UserDetails.class);

            Address mockAddress = new Address(user, new RequestCreateAddressDTO("12345", "서울시 광화문", "ㅁㅁ동 ㅁㅁ호"));
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order mockOrder = spy(new Order(requestCreateOrderDTO, null, 7000, user, mockAddress));
            Payment payment = new Payment(PaymentTypeEnum.COMPLETED);

            when(userDetails.getUsername()).thenReturn("user");
            when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
            when(mockOrder.getUser()).thenReturn(user);
            when(mockOrder.getUpdatedAt()).thenReturn(LocalDateTime.now());
            when(paymentofOrderService.findFirstOrderByUpdatedAtDesc(mockOrder.getOrderId())).thenReturn(Optional.of(payment.getPaymentStatus()));
            RequestUpdateOrderDTO updateDTO = new RequestUpdateOrderDTO("요청사항 수정");

            //when-then
            assertThrows(OrderAlreadyProcessedException.class, () -> orderService.updateOrder(userDetails, UUID.randomUUID(), updateDTO));
        }

        @Test
        @DisplayName("주문 상태변경 성공 테스트(수정)")
        void testUpdateOrderSuccess() {

            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            UserDetails userDetails = mock(UserDetails.class);

            Address mockAddress = new Address(user, new RequestCreateAddressDTO("12345", "서울시 광화문", "ㅁㅁ동 ㅁㅁ호"));
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order mockOrder = spy(new Order(requestCreateOrderDTO, null, 7000, user, mockAddress));
            Payment payment = new Payment(PaymentTypeEnum.PENDING);

            when(userDetails.getUsername()).thenReturn("user");
            when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
            when(mockOrder.getUser()).thenReturn(user);
            when(mockOrder.getUpdatedAt()).thenReturn(LocalDateTime.now());
            when(paymentofOrderService.findFirstOrderByUpdatedAtDesc(mockOrder.getOrderId())).thenReturn(Optional.of(payment.getPaymentStatus()));
            RequestUpdateOrderDTO updateDTO = new RequestUpdateOrderDTO("요청사항 수정");

            //when
            orderService.updateOrder(userDetails, UUID.randomUUID(), updateDTO);

            //then
            assertEquals("요청사항 수정", mockOrder.getComment());
        }


        @DisplayName("주문 상태변경 성공 테스트(삭제)")
        @Test
        void testDeleteOrderSuccess() {

            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            UserDetails userDetails = mock(UserDetails.class);

            Address mockAddress = new Address(user, new RequestCreateAddressDTO("12345", "서울시 광화문", "ㅁㅁ동 ㅁㅁ호"));
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order mockOrder = spy(new Order(requestCreateOrderDTO, null, 7000, user, mockAddress));
            Payment payment = new Payment(PaymentTypeEnum.PENDING);

            when(userDetails.getUsername()).thenReturn("user");
            when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockOrder));
            when(mockOrder.getUser()).thenReturn(user);
            when(mockOrder.getUpdatedAt()).thenReturn(LocalDateTime.now());
            when(paymentofOrderService.findFirstOrderByUpdatedAtDesc(mockOrder.getOrderId())).thenReturn(Optional.of(payment.getPaymentStatus()));
            RequestUpdateOrderDTO updateDTO = new RequestUpdateOrderDTO("요청사항 수정");

            //when
            orderService.deleteOrder(userDetails, UUID.randomUUID());

            //then
            assertEquals("주문 취소", mockOrder.getOrderStatus());
        }

    }

}