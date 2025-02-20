package com.sparta.team30.payment.controller;

import com.sparta.team30.common.exception.ErrorResponse;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.dto.RequestPaymentByOrderId;
import com.sparta.team30.payment.dto.ResponseCreatePaymentDTO;
import com.sparta.team30.payment.dto.ResponsePaymentDetailDTO;
import com.sparta.team30.payment.dto.ResponsePaymentHistoryDTO;
import com.sparta.team30.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Tag(name = "Payment API", description = "결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    @Operation(summary = "결제하기", description = "주문에 대하여 결제 수행")

    @ApiResponse(responseCode = "200", description = "결제 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<ResponseCreatePaymentDTO> makePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RequestPaymentByOrderId requestPaymentByOrderId) {
        ResponseCreatePaymentDTO responseCreatePaymentDTO = paymentService.makePayment(userDetails, requestPaymentByOrderId);
        return ResponseEntity.ok(responseCreatePaymentDTO);
    }
    @ApiResponse(responseCode = "200", description = "주문 결제내역리스트 조회 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{order-id}/history")
    public ResponseEntity<List<ResponsePaymentHistoryDTO>> getPaymentHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId) {

        List<ResponsePaymentHistoryDTO> paymentList = paymentService.getPaymentHistory(userDetails, orderId);

        return ResponseEntity.ok(paymentList);
    }
    @ApiResponse(responseCode = "200", description = "결제상세내역 조회 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 결제",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{payment-id}")
    public ResponseEntity<ResponsePaymentDetailDTO> getPaymentDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("payment-id") UUID paymentId) {

        ResponsePaymentDetailDTO paymentDetailDTO = paymentService.getPaymentDetail(userDetails, paymentId);
        return ResponseEntity.ok(paymentDetailDTO);
    }


    @ApiResponse(responseCode = "200", description = "결제 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"결제가 취소되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("{payment-id}")
    public ResponseEntity<Map<String, String>> deletePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("payment-id") UUID paymentId) {

        paymentService.deletePayment(userDetails,paymentId);

        return ResponseEntity.ok(Map.of("message", "결제가 취소되었습니다."));
    }
}
