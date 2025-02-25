package com.sparta.team30.payment.service;


import com.sparta.team30.payment.domain.PaymentTypeEnum;
import com.sparta.team30.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentOfOrderService {

    private final PaymentRepository paymentRepository;

    public Optional<PaymentTypeEnum> findFirstOrderByUpdatedAtDesc(UUID orderId){
        return paymentRepository.findFirstByOrderOrderByUpdatedAtDesc(orderId);
    }
}
