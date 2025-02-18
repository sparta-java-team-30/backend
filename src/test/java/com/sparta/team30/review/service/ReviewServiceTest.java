package com.sparta.team30.review.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.StoreRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;

import java.util.Optional;
import java.util.UUID;

import com.sparta.team30.user.domain.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Test
    @DisplayName("리뷰 등록 성공 테스트")
    public void testAddReview() {
        // given
        UUID storeId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        int score = 5;
        String content = "맜있어요!";
        ReviewRequestDto requestDto = new ReviewRequestDto(storeId, orderId, score, content);

        // 실제 객체를 사용하여 더미 Store와 Order 생성
        Store dummyStore = createDummyStore(storeId);
        Order dummyOrder = createDummyOrder(orderId);

        // Repository stub 설정
        when(orderRepository.findByOrderIdAndIsDeletedFalse(any(UUID.class)))
                .thenAnswer(invocation -> {
                    // invocation이 이 블록 안에서만 유효
                    UUID passedOrderId = invocation.getArgument(0);
                    System.out.println("orderRepository.findByOrderIdAndIsDeletedFalse 호출됨, 인자: " + passedOrderId);

                    // 필요하다면 여기서 dummyOrder를 반환
                    return Optional.of(dummyOrder);
                });

        when(storeRepository.findByStoreIdAndIsDeletedFalse(any(UUID.class)))
                .thenAnswer(invocation -> {
                    UUID passedStoreId = invocation.getArgument(0);
                    System.out.println("storeRepository.findByStoreIdAndIsDeletedFalse 호출됨, 인자: " + passedStoreId);
                    return Optional.of(dummyStore);
                });

        when(reviewRepository.existsByOrderId(dummyOrder)).thenReturn(false);
        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 실제 User 객체 생성
        User dummyUser = createDummyUser();

        // when
        Review review = reviewService.addReview(requestDto, dummyUser);

        // then
        assertNotNull(review);
        assertEquals(score, review.getScore());
        assertEquals(content, review.getContent());
        assertEquals(dummyUser, review.getUser());
    }



    private User createDummyUser() {
        return new User("test@naver.com", "test", "12321", "dummyUsername", true, UserRoleEnum.USER);
    }

    private Store createDummyStore(UUID storeId) {
        Store store = new Store();
        store.setStoreId(storeId);
        store.setIsDeleted(false);
        // 필요한 다른 필드 초기화 가능
        return store;
    }

    private Order createDummyOrder(UUID orderId) {
        Order order = new Order();
        // Order는 setter가 없으므로 ReflectionTestUtils로 필드 주입
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ReflectionTestUtils.setField(order, "isDeleted", false);
        // 필요한 다른 필드도 주입
        return order;
    }
}
