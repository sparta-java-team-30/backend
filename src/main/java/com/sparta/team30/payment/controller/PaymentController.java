package com.sparta.team30.payment.controller;

import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.dto.RequestPaymentByOrderId;
import com.sparta.team30.payment.dto.ResponseCreatePaymentDTO;
import com.sparta.team30.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ResponseCreatePaymentDTO> makePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RequestPaymentByOrderId requestPaymentByOrderId) {
        ResponseCreatePaymentDTO responseCreatePaymentDTO = paymentService.makePayment(requestPaymentByOrderId);
        return ResponseEntity.ok(responseCreatePaymentDTO);
    }

}
