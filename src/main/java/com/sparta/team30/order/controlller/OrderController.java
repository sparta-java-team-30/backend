package com.sparta.team30.order.controlller;

import com.sparta.team30.infrastructure.security.UserDetailsImpl;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.dto.RequestUpdateOrderDTO;
import com.sparta.team30.order.dto.ResponseOrderDetailsDTO;
import com.sparta.team30.order.dto.ResponseOrderHistoryDTO;
import com.sparta.team30.order.service.OrderService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Map> addOrder(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody RequestCreateOrderDTO requestCreateOrderDTO) {
        String username = userDetails.getUsername();

            orderService.addOrder(username,
                    requestCreateOrderDTO);
        return ResponseEntity.status(200).body(Map.of("message", "주문이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Page> getOrderList(@AuthenticationPrincipal UserDetails userDetails,
                      @RequestParam("page") int page,
                      @RequestParam(value = "size", defaultValue = "10") int size,
                //      @RequestParam("sortBy") String sortBy,
                      @RequestParam("isAsc") boolean isAsc,
                      @RequestParam(value = "search", required = false) String search) {
        //String username = userDetails.getUsername();
        Page<ResponseOrderHistoryDTO> orderHistory = orderService.getOrderHistory(userDetails, search, page, size, isAsc);
        return ResponseEntity.ok(orderHistory);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<ResponseOrderDetailsDTO> getOrderDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(userDetails,orderId));
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity<Map<String, String>> updateOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId,
            @RequestBody RequestUpdateOrderDTO orderDTO) {
        orderService.updateOrder(userDetails,orderId,orderDTO);

        return ResponseEntity.status(200).body(Map.of("message", "주문이 수정되었습니다."));
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<Map<String, String>> deleteOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("order-id") UUID orderId) {

        orderService.deleteOrder(userDetails, orderId);

        return ResponseEntity.status(200).body(Map.of("message", "주문이 삭제되었습니다."));
    }

}
