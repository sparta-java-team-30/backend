package com.sparta.team30.order.controlller;

import com.sparta.team30.infrastructure.security.UserDetailsImpl;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> addOrder(//@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody RequestCreateOrderDTO requestCreateOrderDTO) {

        //String username = userDetails.getUsername();

        //전역 예외 처리 적용 전
        try{
            orderService.addOrder(//user,
                    requestCreateOrderDTO);
            return ResponseEntity.ok("주문이 완료되었습니다.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page> getOrderList(//@AuthenticationPrincipal UserDetailsImpl userDetails,
                      @RequestParam("page") int page,
                      @RequestParam("size") int size,
                      @RequestParam("sortBy") String sortBy,
                      @RequestParam("isAsc") boolean isAsc,
                      @RequestParam("search") String search) {
        //String username = userDetails.getUsername();
        Page<ResponseOrderHistoryDTO> orderHistory = orderService.getOrderHistory(search, page, size, sortBy, isAsc);
        return ResponseEntity.ok(orderHistory);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<ResponseOrderDetailsDTO> getOrderDetails(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

}
