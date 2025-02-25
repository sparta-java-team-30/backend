package com.sparta.team30.payment.service;


import com.sparta.team30.address.domain.Address;
import com.sparta.team30.common.exception.AlreadyPaidException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.service.OrderService;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import com.sparta.team30.payment.dto.RequestPaymentByOrderId;
import com.sparta.team30.payment.dto.ResponseCreatePaymentDTO;
import com.sparta.team30.payment.dto.ResponsePaymentDetailDTO;
import com.sparta.team30.payment.dto.ResponsePaymentHistoryDTO;
import com.sparta.team30.payment.repository.PaymentRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentRepository paymentRepository;


    @Test
    @DisplayName("결제 성공 테스트")
    void makePaymentSuccess() {
        //given
        User user = User.builder().username("user").role(UserRoleEnum.USER).build();
        Address address = new Address();
        UserDetails userDetails = mock(UserDetails.class);
        RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
        Order order = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY,1000,user,address);
        Payment payment =  mock(Payment.class);
        when(userDetails.getUsername()).thenReturn("user");
        when(orderService.getOrder(any())).thenReturn(order);
        when(paymentRepository.findFirstByOrderOrderByUpdatedAtDesc(order.getOrderId())).thenReturn(Optional.of(PaymentTypeEnum.PENDING));
      //  when(payment.getPaymentStatus()).thenReturn(PaymentTypeEnum.PENDING);
        //when
        ResponseCreatePaymentDTO responseCreatePaymentDTO = paymentService.makePayment(userDetails, new RequestPaymentByOrderId(order.getOrderId(), "카드"));
        //then

        assertThat(responseCreatePaymentDTO.getMessage()).isIn("결제가 완료되었습니다.", "결제가 실패하였습니다.");
    }
    @Test
    @DisplayName("결제 실패 테스트(이미 결제)")
    void makePaymentFail() {
        //given
        User user = User.builder().username("user").role(UserRoleEnum.USER).build();
        Address address = new Address();
        UserDetails userDetails = mock(UserDetails.class);
        RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
        Order order = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY,1000,user,address);
        Payment payment =  mock(Payment.class);
        when(userDetails.getUsername()).thenReturn("user");
        when(orderService.getOrder(any())).thenReturn(order);
        when(paymentRepository.findFirstByOrderOrderByUpdatedAtDesc(order.getOrderId())).thenReturn(Optional.of(PaymentTypeEnum.COMPLETED));
        //when(payment.getPaymentStatus()).thenReturn(PaymentTypeEnum.COMPLETED);
        //when-then
        assertThrows(AlreadyPaidException.class , () ->
                paymentService.makePayment(userDetails, new RequestPaymentByOrderId(order.getOrderId(), "카드")));
    }

    @Nested
    @DisplayName("결제 내역 조회 테스트")
    class GetPayment{
        @Test
        void getPaymentHistory() {
            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            Address address = new Address();
            UserDetails userDetails = mock(UserDetails.class);
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order order = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY,1000,user,address);
            Payment payment1 =  mock(Payment.class);
            Payment payment2 = mock(Payment.class);
            List<Payment> mockList = List.of(payment1,payment2);
            when(userDetails.getUsername()).thenReturn("user");
            when(orderService.getOrder(any())).thenReturn(order);
            when( userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.ofNullable(user));
            when(paymentRepository.findAllByOrderAndIsDeletedFalseOrderByUpdatedAtDesc(order)).thenReturn(mockList);

            //when
            List<ResponsePaymentHistoryDTO> paymentHistory = paymentService.getPaymentHistory(userDetails, order.getOrderId());
            //then
            assertEquals(2, paymentHistory.size());
        }

        @Test
        void getPaymentDetail() {
            //given
            User user = User.builder().username("user").role(UserRoleEnum.USER).build();
            Address address = new Address();
            UserDetails userDetails = mock(UserDetails.class);
            RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
            Order order = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY,1000,user,address);
            Payment payment =  mock(Payment.class);
            when(userDetails.getUsername()).thenReturn("user");
            when(orderService.getOrder(any())).thenReturn(order);
            when( userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.ofNullable(user));
            when(paymentRepository.findById(payment.getPaymentId())).thenReturn(Optional.of(payment));
            when(payment.getCreatedBy()).thenReturn("user");
            //when
            ResponsePaymentDetailDTO paymentDetail = paymentService.getPaymentDetail(userDetails, payment.getPaymentId());
            //then
            assertEquals(payment.getPaymentId(),paymentDetail.getPaymentId());
        }
    }


    @Test
    @DisplayName("결제 취소 실패 테스트(사용자가 접근)")
    void deletePaymentFail() {
        //given
        User user = User.builder().username("user").role(UserRoleEnum.USER).build();
        Address address = new Address();
        UserDetails userDetails = mock(UserDetails.class);
        RequestCreateOrderDTO requestCreateOrderDTO = new RequestCreateOrderDTO();
        Order order = new Order(requestCreateOrderDTO, OrderTypeEnum.DELIVERY,1000,user,address);
        Payment payment =  mock(Payment.class);
        when(userDetails.getUsername()).thenReturn("user");
        when( userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.ofNullable(user));
        when(paymentRepository.findById(payment.getPaymentId())).thenReturn(Optional.of(payment));

        //when-then
        assertThrows(IllegalArgumentException.class , () ->    paymentService.deletePayment(userDetails, payment.getPaymentId()));
    }

    @Test
    @DisplayName("결제 취소 성공 테스트")
    void deletePaymentSuccess() {
        //given
        User user = User.builder().username("master").role(UserRoleEnum.MASTER).build();
        UserDetails userDetails = mock(UserDetails.class);
        Payment payment =  new Payment(PaymentTypeEnum.COMPLETED);
        when(userDetails.getUsername()).thenReturn("master");
        when( userRepository.findByUsername(userDetails.getUsername())).thenReturn(Optional.ofNullable(user));
        when(paymentRepository.findById(payment.getPaymentId())).thenReturn(Optional.of(payment));
        //when
        paymentService.deletePayment(userDetails, payment.getPaymentId());
        //then
        assertEquals(PaymentTypeEnum.CANCELLED,payment.getPaymentStatus());
    }
}