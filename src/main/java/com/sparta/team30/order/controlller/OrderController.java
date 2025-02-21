package com.sparta.team30.order.controlller;

import com.sparta.team30.common.exception.ErrorResponse;
import com.sparta.team30.infrastructure.security.UserDetailsImpl;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.dto.RequestUpdateOrderDTO;
import com.sparta.team30.order.dto.ResponseOrderDetailsDTO;
import com.sparta.team30.order.dto.ResponseOrderHistoryDTO;
import com.sparta.team30.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.Map;
import java.util.UUID;
@Tag(name = "Order API", description = "주문 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    @Operation(summary = "주문 추가", description = "주문을 추가합니다.")

    @ApiResponse(responseCode = "200", description = "주문 추가 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"주문 생성이 완료되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "사용자 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<Map> addOrder(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody @Valid RequestCreateOrderDTO requestCreateOrderDTO) {
        String username = userDetails.getUsername();

            orderService.addOrder(username,
                    requestCreateOrderDTO);
        return ResponseEntity.status(200).body(Map.of("message", "주문 생성이 완료되었습니다."));
    }
    @ApiResponse(responseCode = "200", description = "주문리스트 조회 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<Page> getOrderList(@AuthenticationPrincipal UserDetails userDetails,
                      @RequestParam(value = "page", defaultValue = "0") int page,
                      @RequestParam(value = "size", defaultValue = "10") int size,
                //      @RequestParam("sortBy") String sortBy,
                      @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc,
                      @RequestParam(value = "search", required = false) String search) {
        //String username = userDetails.getUsername();
        Page<ResponseOrderHistoryDTO> orderHistory = orderService.getOrderHistory(userDetails, search, page, size, isAsc);
        return ResponseEntity.ok(orderHistory);
    }
    @ApiResponse(responseCode = "200", description = "주문상세 조회 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "406", description = "접근 권한 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{order-id}")
    public ResponseEntity<ResponseOrderDetailsDTO> getOrderDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(userDetails,orderId));
    }


    @ApiResponse(responseCode = "200", description = "주문 수정 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"주문이 수정되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{order-id}")
    public ResponseEntity<Map<String, String>> updateOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId,
            @RequestBody @Valid RequestUpdateOrderDTO orderDTO) {
        orderService.updateOrder(userDetails,orderId,orderDTO);

        return ResponseEntity.status(200).body(Map.of("message", "주문이 수정되었습니다."));
    }


    @ApiResponse(responseCode = "200", description = "주문 삭제 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                            name="200 OK",
                            value = "{\"message\" : \"주문이 삭제되었습니다.\"}"
                    )))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "존재하지 않는 주문",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{order-id}")
    public ResponseEntity<Map<String, String>> deleteOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId) {

        orderService.deleteOrder(userDetails, orderId);

        return ResponseEntity.status(200).body(Map.of("message", "주문이 삭제되었습니다."));
    }

}
