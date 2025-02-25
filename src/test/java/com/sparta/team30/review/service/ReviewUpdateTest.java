package com.sparta.team30.review.service;

import com.sparta.team30.common.domain.BaseEntity;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewUpdateRequestDto;
import com.sparta.team30.review.exception.ReviewTimeExpiredException;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        store.setStoreGrade(4.0);
        store.setStoreReviewCount(2);

        Order order = new Order();

        review = new Review(3, "원래 리뷰 내용", store, order, user);
        review.setReviewId(reviewId);

        // 기본적으로 createdAt을 2일 전으로 설정 (3일 이내)
        try {
            java.lang.reflect.Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(review, LocalDateTime.now().minusDays(2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 모든 테스트에 공통으로 필요한 스터빙만 설정
        when(reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)).thenReturn(Optional.of(review));
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    public void testUpdateSuccessReview() {
        // Given
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(reviewId, 4, "수정된 리뷰 내용");
        String username = "testUser";


        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        reviewService.updateReview(requestDto, username);

        // Then
        assertEquals(4, review.getScore(), "리뷰 점수가 4로 수정되어야 합니다.");
        assertEquals("수정된 리뷰 내용", review.getContent(), "리뷰 내용이 수정되어야 합니다.");

        verify(storeRepository, times(1)).findByStoreIdAndIsDeletedFalse(storeId);
        verify(storeRepository, times(1)).save(store);
        assertEquals(2, store.getStoreReviewCount(), "리뷰 수는 변경되지 않아야 합니다.");
        assertEquals(4.5, store.getStoreGrade(), 0.01, "평균 평점이 (4.0 * 2 - 3 + 4) / 2 = 4.5로 재계산되어야 합니다.");

        verify(reviewRepository, times(1)).findByReviewIdAndIsDeletedFalse(reviewId);
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 3일 초과")
    public void testUpdateReviewTimeExpired() throws Exception {
        // Given
        // createdAt을 4일 전으로 설정 (3일 초과)
        java.lang.reflect.Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
        createdAtField.setAccessible(true);
        createdAtField.set(review, LocalDateTime.now().minusDays(4));

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(reviewId, 4, "수정된 리뷰 내용");
        String username = "testUser";

        // When & Then
        assertThrows(ReviewTimeExpiredException.class, () -> {
            reviewService.updateReview(requestDto, username);
        }, "리뷰 생성 후 3일이 지나면 수정 시 ReviewTimeExpiredException이 발생해야 합니다.");

        // 추가 검증
        assertEquals(3, review.getScore(), "3일 초과");
        assertEquals("원래 리뷰 내용", review.getContent(), "3일 초과");
    }
}