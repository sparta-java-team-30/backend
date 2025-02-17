package com.sparta.team30.order.controlller;

import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> addOrder(//@AuthenticationPrincipal UserDetails userdetails,
                                            @RequestBody RequestCreateOrderDTO requestCreateOrderDTO) {

        //전역 예외 처리 적용 전
        try{
            orderService.addOrder(//user,
                    requestCreateOrderDTO);
            return ResponseEntity.ok("주문이 완료되었습니다.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
