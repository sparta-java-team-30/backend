package com.sparta.team30.payment.controller;

import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.dto.RequestPaymentByOrderId;
import com.sparta.team30.payment.dto.ResponseCreatePaymentDTO;
import com.sparta.team30.payment.dto.ResponsePaymentDetailDTO;
import com.sparta.team30.payment.dto.ResponsePaymentHistoryDTO;
import com.sparta.team30.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ResponseCreatePaymentDTO> makePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RequestPaymentByOrderId requestPaymentByOrderId) {
        ResponseCreatePaymentDTO responseCreatePaymentDTO = paymentService.makePayment(userDetails, requestPaymentByOrderId);
        return ResponseEntity.ok(responseCreatePaymentDTO);
    }

    @GetMapping("/{order-id}/history")
    public ResponseEntity<List<ResponsePaymentHistoryDTO>> getPaymentHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId) {

        List<ResponsePaymentHistoryDTO> paymentList = paymentService.getPaymentHistory(userDetails, orderId);

        return ResponseEntity.ok(paymentList);
    }

    @GetMapping("/{payment-id}")
    public ResponseEntity<ResponsePaymentDetailDTO> getPaymentDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("payment-id") UUID paymentId) {

        ResponsePaymentDetailDTO paymentDetailDTO = paymentService.getPaymentDetail(userDetails, paymentId);
        return ResponseEntity.ok(paymentDetailDTO);
    }

    @DeleteMapping("{payment-id}")
    public ResponseEntity<Map<String, String>> deletePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("payment-id") UUID paymentId) {

        paymentService.deletePayment(userDetails,paymentId);

        return ResponseEntity.ok(Map.of("message", "결제가 취소되었습니다."));
    }
}
