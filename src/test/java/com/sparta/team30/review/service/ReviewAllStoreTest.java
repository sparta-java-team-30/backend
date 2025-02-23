package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ReviewAllStoreTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreRepository storeRepository;


    private UUID storeId;
    private Store store;
    public Pageable pageable;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        store = new Store();
        store.setStoreId(storeId);
        pageable = PageRequest.of(0, 5); // 첫 페이지, 5개 항목
    }
    @Test
    @DisplayName("특정 가게의 리뷰 조회 성공 (keyword 없음)")
    public void testFindAllReviewByStore_NoKeyword() {
        // Given
        User user = new User("test1234@naver.com", "testUser", "test123!", "test", false, UserRoleEnum.USER);
        Order order = new Order(); // Order 객체 생성 (필요 시 Order 클래스의 생성자 사용)
        List<Review> reviewList = Arrays.asList(
                new Review(1, "배달이 느려요", store, order, user),
                new Review(2, "맛 없어요", store, order, user),
                new Review(3, "배달이 식어서 왔어요", store, order, user)
        );
        Page<Review> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());

        // Mock 설정
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.findAllByStoreIdAndIsDeletedFalseWithKeyword(storeId, null, pageable))
                .thenReturn(reviewPage);

        // When
        Page<Review> response = reviewService.findAllReviewByStore(storeId, null, pageable);

        // Then
        assertEquals(3, response.getTotalElements(), "리뷰 총 개수가 3개여야 합니다.");
        assertEquals("배달이 느려요", response.getContent().get(0).getContent(), "첫 번째 리뷰 내용이 일치해야 합니다.");
        assertEquals(2, response.getContent().get(1).getScore(), "두 번째 리뷰 점수가 2여야 합니다.");

        // Verify
        verify(storeRepository, times(1)).findByStoreIdAndIsDeletedFalse(storeId);
        verify(reviewRepository, times(1)).findAllByStoreIdAndIsDeletedFalseWithKeyword(storeId, null, pageable);
    }
}
