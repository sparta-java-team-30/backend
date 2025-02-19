package com.sparta.team30.payment.service;

import com.sparta.team30.common.exception.AlreadyPaidException;
import com.sparta.team30.common.exception.OrderNotFoundException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import com.sparta.team30.payment.dto.RequestPaymentByOrderId;
import com.sparta.team30.payment.dto.ResponseCreatePaymentDTO;
import com.sparta.team30.payment.dto.ResponsePGPaymentDTO;
import com.sparta.team30.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public ResponseCreatePaymentDTO makePayment(RequestPaymentByOrderId requestPaymentByOrderId) {

        Order order = orderRepository.findById(requestPaymentByOrderId.getOrderId()).orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문입니다."));

        Optional<Payment> latestPayment = paymentRepository.findFirstByOrderOrderByUpdatedAtDesc(order);
        if (latestPayment.isPresent() && latestPayment.get().getPaymentStatus().equals(PaymentTypeEnum.COMPLETED)) {
            throw new AlreadyPaidException("이미 결제된 주문입니다.");
        }

        //결제 정보 초기 상태
        Payment payment = new Payment(order, requestPaymentByOrderId);
        paymentRepository.save(payment);

        //결제 외부 pg사로 요청 후 응답 메시지 받아온다고 가정.
        //ResponsePaymentDTO responsePaymentDTO = requestPaymentToPG(paymentSampleDTO);
        ResponsePGPaymentDTO responsePGPaymentDTO;

        double v = Math.random() * 10;

        if(v<=8){ //결제 성공 확률 80%
            responsePGPaymentDTO = new ResponsePGPaymentDTO("국민은행","카드결제",true);
            order.updateStatus("주문 완료");
        } else{
            responsePGPaymentDTO = new ResponsePGPaymentDTO("국민은행","카드결제",false);
        }

        Boolean update = payment.update(responsePGPaymentDTO);


        return new ResponseCreatePaymentDTO(payment.getPaymentId(), update);
    }


}
