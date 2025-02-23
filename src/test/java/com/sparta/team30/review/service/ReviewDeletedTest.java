package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.repository.StoreRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ReviewDeletedTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;


    private Review review;
    private Store store;
    private UUID storeId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        User user = new User("test1234@naver.com", "test1234", "test123!", "test", false, UserRoleEnum.USER);
        store = new Store(); // 기본 생성자 사용
        store.setStoreId(storeId);
        store.setStoreGrade(4.0); // 초기 평균 평점
        store.setStoreReviewCount(2); // 초기 리뷰 수

        Order order = new Order();


        review = new Review(5, "맛있어요!", store, order, user);
        review.setReviewId(UUID.randomUUID());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    public void testDeleteReview() {
        // Given
        String username = "test1234";
        when(reviewRepository.findByReviewIdAndIsDeletedFalse(review.getReviewId())).thenReturn(Optional.of(review));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(any(UUID.class))).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        reviewService.deleteReview(review.getReviewId(), username);

        // Then
        assertTrue("리뷰가 삭제 상태로 변경되어야 합니다.",review.getIsDeleted());
        verify(reviewRepository, times(1)).findByReviewIdAndIsDeletedFalse(review.getReviewId());
        verify(storeRepository, times(1)).findByStoreIdAndIsDeletedFalse(storeId);
        verify(storeRepository, times(1)).save(store);
        assertEquals(1, store.getStoreReviewCount(), "리뷰 수가 1 감소해야 합니다.");
        assertEquals(3.0, store.getStoreGrade(), 0.01, "평균 평점이 재계산되어야 합니다."); // delta로 소수점 비교
    }
}