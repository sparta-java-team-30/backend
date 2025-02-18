package com.sparta.team30.order.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.order.dto.*;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;
    @Transactional
    public ResponseCreateOrderDTO addOrder(//User user,
                                RequestCreateOrderDTO requestCreateOrderDTO) {

        //고객 = 배달, 가게 주인 = 포장. 추후 추가 예정.
        /*User user;
        UserRoleEnum role = user.getRole();
        OrderTypeEnum orderType = OrderTypeEnum.DELIVERY;
        if(role.getAuthority().equals("ROLE_ADMIN")) {
            orderType = OrderTypeEnum.PICKUP;
        }*/

        //고객 주소
        //Address address = addressRepository.findByUser(user);

        //전역예외처리
        if (requestCreateOrderDTO.getProductList().isEmpty()) {
            throw new IllegalArgumentException("선택 상품이 존재하지 않습니다");
        }
        List<RequestOrderProductDTO> productDTOList = requestCreateOrderDTO.getProductList();

        //전역예외처리
        productDTOList.stream().forEach(productDTO -> {
            if(productDTO.getProductId()==null){
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }
        });
        int totalPrice = 0;
        for (RequestOrderProductDTO product : productDTOList) {
            totalPrice+=product.getPrice()*product.getQuantity();
        }
        Order order = new Order(requestCreateOrderDTO,OrderTypeEnum.DELIVERY, totalPrice
                //user,
                //address
                );
        orderRepository.save(order);

        List<UUID> productIdList = productDTOList.stream().map(RequestOrderProductDTO::getProductId).collect(Collectors.toList());
        List<Product> productList = productRepository.findAllById(productIdList);

        //전역예외처리
        if (productIdList.isEmpty()) {
            throw new IllegalArgumentException("선택 상품이 존재하지 않습니다");
        }
        //주문 상세 테이블에 주문 상품 추가.
        orderDetailService.addOrderProducts(order, productList);

        return new ResponseCreateOrderDTO("주문이 완료되었습니다."
            //,orderType
        );
    }

    public Page<ResponseOrderHistoryDTO> getOrderHistory(
      // User user,
        String search, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction= isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // USER는 본인 주문 내역 조회, //MANAGER는 모든 사용자 주문 내역 조회 ( 추가 예정 )
        //사용자 주문 내역 조회 ( 임시로 1L )
        Page<ResponseOrderHistoryDTO> orderHistoryList = orderRepository.findByUserIdAndProductOrStoreName(search, 1L, pageable);
        return orderHistoryList;
    }

    public ResponseOrderDetailsDTO getOrderDetails(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        List<ResponseOrderProductDTO> orderProductList = orderDetailService.getOrderProductList(orderId);

        return new ResponseOrderDetailsDTO(order,orderProductList);
    }

}
