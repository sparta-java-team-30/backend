package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewResponseDto;
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
public class ReviewFindByReviewListTest {

    @InjectMocks
    private ReviewService reviewService;

    // 이미 선언된 Mock
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreRepository storeRepository;

    // 1) 사용자 조회 테스트가 필요하면 UserRepository도 Mock으로 선언
    @Mock
    private UserRepository userRepository;

    // 테스트용 필드
    private UUID storeId;
    private Store store;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        store = new Store();
        store.setStoreId(storeId);
        pageable = PageRequest.of(0, 5); // 첫 페이지, 5개 항목
    }

    @Test
    @DisplayName("특정 가게의 리뷰 조회 성공")
    public void testFindAllReviewByStore() {
        // Given
        User user = new User("test1234@naver.com", "testUser", "test123!", "test", false, UserRoleEnum.USER);
        Order order = new Order();
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
        assertEquals(3, response.getTotalElements(), "가게 리뷰 총 갯수");
        assertEquals("배달이 느려요", response.getContent().get(0).getContent(), "리뷰 내용 확인");
        assertEquals(2, response.getContent().get(1).getScore(), "리뷰 점수 확인");

        // Verify
        verify(storeRepository, times(1)).findByStoreIdAndIsDeletedFalse(storeId);
        verify(reviewRepository, times(1))
                .findAllByStoreIdAndIsDeletedFalseWithKeyword(storeId, null, pageable);
    }


    @Test
    @DisplayName("리뷰 사용자 조회")
    public void testListUserReview() {
        // Given
        User user = new User("test1234@naver.com", "testUser", "test123!", "test", false, UserRoleEnum.USER);
        Store store = new Store();
        Order order = new Order();
        List<Review> reviewList = List.of(
                new Review(1, "배달이 느려요", store, order, user),
                new Review(2, "맛 없어요", store, order, user),
                new Review(3, "배달이 식어서 왔어요", store, order, user),
                new Review(4, "맛있어요", store, order, user),
                new Review(5, "음식이 간이 세지않으면서 너무 맛있어요!", store, order, user)
        );

        // Mock 설정
        when(userRepository.findUserByUserId("testUser")).thenReturn(Optional.of(user));
        when(reviewRepository.findAllByUserIdAndIsDeletedFalse(user.getId())).thenReturn(reviewList);

        // When
        List<ReviewResponseDto> response = reviewService.findAllReviewByUser("testUser");

        // Then
        assertEquals(5, response.size()); // 리뷰 개수 확인
        assertEquals("배달이 느려요", response.get(0).getContent()); // 첫 번째 리뷰 내용 확인
        assertEquals(5, response.get(4).getScore()); // 마지막 리뷰 점수 확인

        // Verify
        verify(userRepository, times(1)).findUserByUserId("testUser");
        verify(reviewRepository, times(1)).findAllByUserIdAndIsDeletedFalse(user.getId());
    }


}
