package com.sparta.team30.order.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderDetail;
import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.order.dto.RequestCreateOrderDTO;
import com.sparta.team30.order.dto.RequestOrderProductDTO;
import com.sparta.team30.order.dto.ResponseCreateOrderDTO;
import com.sparta.team30.order.repository.OrderDetailRepository;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Order order = new Order(requestCreateOrderDTO,OrderTypeEnum.DELIVERY
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
}
