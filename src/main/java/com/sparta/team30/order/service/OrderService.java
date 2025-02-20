package com.sparta.team30.order.service;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.address.repository.AddressRepository;
import com.sparta.team30.common.exception.AddressNotFoundException;
import com.sparta.team30.common.exception.OrderAccessDeniedException;
import com.sparta.team30.common.exception.OrderAlreadyProcessedException;
import com.sparta.team30.common.exception.OrderNotFoundException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.domain.OrderTypeEnum;
import com.sparta.team30.order.dto.*;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.payment.domain.Payment;
import com.sparta.team30.payment.domain.PaymentTypeEnum;
import com.sparta.team30.payment.repository.PaymentRepository;
import com.sparta.team30.products.domain.Product;
import com.sparta.team30.products.repository.ProductRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
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
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    @Transactional
    public ResponseCreateOrderDTO addOrder(String username,
                                RequestCreateOrderDTO requestCreateOrderDTO) {

        //고객 = 배달, 가게 주인 = 포장
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found " + username));
        UserRoleEnum role = user.getRole();
        OrderTypeEnum orderType = OrderTypeEnum.DELIVERY;
        if(role.getAuthority().equals("ROLE_ADMIN")) {
            orderType = OrderTypeEnum.PICKUP;
        }

        //고객 주소
        Address address = addressRepository.findByUsernameAndAddressIsDefault(user.getUsername());
        if(address==null){
            throw new AddressNotFoundException("기본 배송지를 선택해 주세요.");
        }

        //전역예외처리 ( 상품 예외)
        if (requestCreateOrderDTO.getProductList().isEmpty()) {
            throw new IllegalArgumentException("선택 상품이 존재하지 않습니다");
        }
        List<RequestOrderProductDTO> productDTOList = requestCreateOrderDTO.getProductList();

        //전역예외처리 (상품 예외 )
        productDTOList.stream().forEach(productDTO -> {
            if(productDTO.getProductId()==null){
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }
        });
        int totalPrice = 0;
        for (RequestOrderProductDTO product : productDTOList) {
            totalPrice+=product.getPrice()*product.getQuantity();
        }
        Order order = new Order(requestCreateOrderDTO,orderType, totalPrice, user ,address);
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
        UserDetails userDetails,
        String search, int page, int size,  boolean isAsc) {
        Sort.Direction direction= isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        // USER는 본인 주문 내역 조회, //OWNER는 본인 가게 주문만 조회, //MANAGER는 모든 사용자 주문 내역 조회
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not found " + userDetails.getUsername()));

        Page<ResponseOrderHistoryDTO> orderHistoryList = orderRepository.findByUserIdAndProductOrStoreName(search, user.getId(), user.getRole(),user.getUsername(), pageable, isAsc);
        return orderHistoryList;
    }

    public ResponseOrderDetailsDTO getOrderDetails(UserDetails userDetails, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if(order.getUser().getRole().equals(UserRoleEnum.USER) && !order.getUser().getUsername().equals(userDetails.getUsername())){
            throw new OrderAccessDeniedException("권한이 없습니다.");
        }
        List<ResponseOrderProductDTO> orderProductList = orderDetailService.getOrderProductList(orderId);

        String storeName = orderDetailService.getStoreName(orderId);

        return new ResponseOrderDetailsDTO(order,order.getAddress(), storeName, orderProductList);
    }

    @Transactional
    public void updateOrder(UserDetails userDetails, UUID orderId, RequestUpdateOrderDTO orderDTO) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문입니다."));

        checkAutority(userDetails,order);

        order.update(orderDTO);
    }

    @Transactional
    public void deleteOrder(UserDetails userDetails, UUID orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문입니다."));

        checkAutority(userDetails, order);

        order.deleteOrder(order.getUser().getUsername(),"주문 취소");
    }

    private void checkAutority(UserDetails userDetails, Order order) {

        //사용자일 경우에만 체크
        if(order.getUser().getRole().equals(UserRoleEnum.USER)) {

            if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
                throw new OrderAccessDeniedException("권한이 없습니다.");
            }

            if (order.getUpdatedAt().isBefore(LocalDateTime.now().minusMinutes(5)) && order.getUser().getRole().equals(UserRoleEnum.USER)) //사용자는 5분 이내
            {
                throw new OrderAlreadyProcessedException("이미 접수된 주문입니다.(5분 초과)");
            }
        }
        //MASTER 제외 결제된 주문은 수정 불가
        if (!order.getUser().getRole().equals(UserRoleEnum.MASTER)) {
            Optional<Payment> findPayment = paymentRepository.findFirstByOrderOrderByUpdatedAtDesc(order);
            if (findPayment.isPresent() && findPayment.get().getPaymentStatus().equals(PaymentTypeEnum.COMPLETED)) {
                throw new OrderAlreadyProcessedException("이미 접수된 주문입니다.(결제 완료)");
            }
        }
    }


}
