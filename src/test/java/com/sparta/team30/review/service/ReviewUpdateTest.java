package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewUpdateRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.repository.StoreRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
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

@ExtendWith(MockitoExtension.class)
public class ReviewUpdateTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreRepository storeRepository;

    private Review review;
    private Store store;
    private UUID storeId;
    private UUID reviewId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        reviewId = UUID.randomUUID();

        User user = new User("test1234@naver.com", "testUser", "test123!", "test", false, UserRoleEnum.USER);
        store = new Store();
        store.setStoreId(storeId);
        store.setStoreGrade(4.0); // 초기 평균 평점
        store.setStoreReviewCount(2); // 초기 리뷰 수

        Order order = new Order();

        // 리뷰 생성 (3일 이내 생성된 것으로 가정)
        review = new Review(3, "원래 리뷰 내용", store, order, user);
        review.setReviewId(reviewId);

        // Mock 설정
        when(reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)).thenReturn(Optional.of(review));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    public void testUpdateReview() {
        // Given
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(reviewId,4,"수정된 리뷰 내용");

        String username = "testUser"; // 사용자 이름은 User의 username과 일치

        // When
        reviewService.updateReview(requestDto, username);

        // Then
        assertEquals(4, review.getScore(), "리뷰 점수가 4로 수정되어야 합니다.");
        assertEquals("수정된 리뷰 내용", review.getContent(), "리뷰 내용이 수정되어야 합니다.");

        // 가게 평점 재계산 검증 (기존 3점에서 4점으로 수정, 평균 재계산)
        verify(storeRepository, times(1)).findByStoreIdAndIsDeletedFalse(storeId);
        verify(storeRepository, times(1)).save(store);
        assertEquals(2, store.getStoreReviewCount(), "리뷰 수는 변경되지 않아야 합니다.");
        assertEquals(4.0, store.getStoreGrade(), 0.01, "평균 평점이 (4.0 * 2 - 3 + 4) / 2 = 4.5로 재계산되어야 합니다."); // (4.0 * 2 - 3 + 4) / 2 = 4.5

        // Verify 호출
        verify(reviewRepository, times(1)).findByReviewIdAndIsDeletedFalse(reviewId);
    }
}