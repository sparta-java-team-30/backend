package com.sparta.team30.review.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewCreateRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.dto.ReviewUpdateRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.dto.StoreRequestDto;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

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

    @Mock
    private UserRepository userRepository;


    private Review review;
    private UUID storeId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        Category category = new Category();
        Order order = new Order();
        User user = new User("test1234@naver.com","test1234","test123!","test",false, UserRoleEnum.USER);
        StoreRequestDto requestDto = new StoreRequestDto(category.getCategoryId(), "한식당1", "0212341234", "01234", "서울시 중구", "1층");
        Store store = new Store(category, requestDto);


        review = new Review(5, "맛있어요!", store, order, user);
    }

    @Test
    @DisplayName("리뷰 등록 성공 테스트")
    public void testAddReview() {
        User user = new User("test1234@naver.com","testUser","test123!","test",false, UserRoleEnum.USER);
        Store store = new Store();
        Order order = new Order();
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(storeId, orderId, 5, "맛있어요");
        when(userRepository.findUserByUserId("testUser")).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderIdAndIsDeletedFalse(orderId)).thenReturn(Optional.of(order));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.existsByOrderId(order)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // When
        ReviewResponseDto response = reviewService.addReview(requestDto, "testUser");

        // Then
        assertEquals(5, response.getScore());
        assertEquals("맛있어요!", response.getContent());
        assertEquals(5.0, store.getStoreGrade());
        assertEquals(1, store.getStoreReviewCount());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }


    @Test
    @DisplayName("리뷰 수정 성공")
    public void testUpdateReview() {
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto();
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    public void testDeleteReview() {
        Store store = new Store(); // 기본 생성자 사용
        store.setStoreId(storeId);
        store.setStoreGrade(4.0); // 초기 평균 평점
        store.setStoreReviewCount(2); // 초기 리뷰 수

        // Given
        String username = "test1234";
        when(reviewRepository.findByReviewIdAndIsDeletedFalse(review.getReviewId())).thenReturn(Optional.of(review));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        reviewService.deleteReview(review.getReviewId(), username);

        // Then
        assertTrue( "리뷰가 삭제 상태로 변경되어야 합니다.",review.getIsDeleted());
        verify(reviewRepository, times(1)).findByReviewIdAndIsDeletedFalse(review.getReviewId());
        verify(storeRepository, times(1)).findByStoreIdAndIsDeletedFalse(storeId);
        verify(storeRepository, times(1)).save(store);
        assertEquals(1, store.getStoreReviewCount(), "리뷰 수가 1 감소해야 합니다.");
        assertEquals(3.0, store.getStoreGrade(), 0.01, "평균 평점이 재계산되어야 합니다.");
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