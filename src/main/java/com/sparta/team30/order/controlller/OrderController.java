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
    public ResponseEntity<Map> addOrder(//@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody RequestCreateOrderDTO requestCreateOrderDTO) {
        //String username = userDetails.getUsername();

            orderService.addOrder(//user,
                    requestCreateOrderDTO);
        return ResponseEntity.status(200).body(Map.of("message", "주문이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Page> getOrderList(//@AuthenticationPrincipal UserDetailsImpl userDetails,
                      @RequestParam("page") int page,
                      @RequestParam(value = "size", defaultValue = "10") int size,
                //      @RequestParam("sortBy") String sortBy,
                      @RequestParam("isAsc") boolean isAsc,
                      @RequestParam(value = "search", required = false) String search) {
        //String username = userDetails.getUsername();
        Page<ResponseOrderHistoryDTO> orderHistory = orderService.getOrderHistory(search, page, size, isAsc);
        return ResponseEntity.ok(orderHistory);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<ResponseOrderDetailsDTO> getOrderDetails(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity<Map<String, String>> updateOrder(@PathVariable("order-id") UUID orderId,
                                                           @RequestBody RequestUpdateOrderDTO orderDTO) {
        orderService.updateOrder(orderId,orderDTO);

        return ResponseEntity.status(200).body(Map.of("message", "주문이 수정되었습니다."));
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable("order-id") UUID orderId) {

        orderService.deleteOrder(orderId);

        return ResponseEntity.status(200).body(Map.of("message", "주문이 삭제되었습니다."));
    }

}
