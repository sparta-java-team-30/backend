package com.sparta.team30.payment.service;

import com.sparta.team30.common.exception.AlreadyPaidException;
import com.sparta.team30.common.exception.OrderAccessDeniedException;
import com.sparta.team30.common.exception.OrderNotFoundException;
import com.sparta.team30.common.exception.PaymentNotFoundException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.order.service.OrderService;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import com.sparta.team30.payment.dto.*;
import com.sparta.team30.payment.repository.PaymentRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    @Transactional
    public ResponseCreatePaymentDTO makePayment(UserDetails userDetails, RequestPaymentByOrderId requestPaymentByOrderId) {

        Order order = orderService.getOrder(requestPaymentByOrderId.getOrderId());

        //본인 생성 주문만 결제 접근 가능
        if(!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new OrderAccessDeniedException("잘못된 접근입니다.");
        }
        //이미 결제된 주문인지 확인
        Optional<PaymentTypeEnum> paymentState = paymentRepository.findFirstByOrderOrderByUpdatedAtDesc(order.getOrderId());
        if (paymentState.isPresent() && paymentState.get()==PaymentTypeEnum.COMPLETED) {
            throw new AlreadyPaidException("이미 결제된 주문입니다.");
        }

        //결제 정보 초기 상태
        Payment payment = new Payment(order, requestPaymentByOrderId);
        paymentRepository.save(payment);

        //결제 외부 pg사로 요청 후 응답 메시지 받아온다고 가정.
        //ResponsePaymentDTO responsePaymentDTO = requestPaymentToPG(paymentSampleDTO);

        //결제 성공 확률 80%
        boolean isSuccess = Math.random() * 10 <= 8;

        if(isSuccess) {
            order.updateStatus("주문 완료");
        }

        ResponsePGPaymentDTO responsePGPaymentDTO = new ResponsePGPaymentDTO("국민은행","카드결제",isSuccess);
        return new ResponseCreatePaymentDTO(payment.getPaymentId(), responsePGPaymentDTO.isSuccess());
    }

    //하나 주문에 대한 결제 내역 조회
    public List<ResponsePaymentHistoryDTO> getPaymentHistory(UserDetails userDetails, UUID orderId) {

        Order order = orderService.getOrder(orderId);
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        //사용자는 본인 주문만 결제내역 조회 가능
        if (user.getRole().equals(UserRoleEnum.USER) && !order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new OrderAccessDeniedException("잘못된 접근입니다.");
        }

        List<Payment> paymentListDesc = paymentRepository.findAllByOrderAndIsDeletedFalseOrderByUpdatedAtDesc(order);

        List<ResponsePaymentHistoryDTO> paymentDTOList = paymentListDesc.stream().map(ResponsePaymentHistoryDTO::new).collect(Collectors.toList());

        return paymentDTOList;
    }
    //결제 상세 조회
    public ResponsePaymentDetailDTO getPaymentDetail(UserDetails userDetails, UUID paymentId) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("존재하지 않는 결제내역입니다."));
        if (user.getRole().equals(UserRoleEnum.USER) && !payment.getCreatedBy().equals(user.getUsername())) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        return new ResponsePaymentDetailDTO(payment);
    }

    //결제 취소 (MASTER, MANAGER, OWNER 만 가능)
    @Transactional
    public void deletePayment(UserDetails userDetails, UUID paymentId) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        if(user.getRole().equals(UserRoleEnum.USER)){
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("존재하지 않는 결제내역입니다."));


        //결제 취소 외부 PG사에 요청 후 성공 응답 왔다고 가정.
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        payment.deletePayment(user.getUsername(),PaymentTypeEnum.CANCELLED);
    }


}
