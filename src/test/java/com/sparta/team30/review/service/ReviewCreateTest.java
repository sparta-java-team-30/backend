package com.sparta.team30.review.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.dto.CategoryDto;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewCreateRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.exception.DuplicateReviewException;
import com.sparta.team30.review.exception.InvalidReviewContentException;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCreateTest {
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
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("한식");
        CategoryDto categoryDto = new CategoryDto(category);
        Order order = new Order();
        User user = new User("test1234@naver.com","test1234","test123!","test",false, UserRoleEnum.USER);
        StoreRequestDto requestDto = new StoreRequestDto(category.getCategoryId(), "한식당1", "0212341234", "01234", "서울시 중구", "1층");
        Store store = new Store(categoryDto, requestDto,user);


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
    @DisplayName("리뷰 등록 실패 - 이미 리뷰가 존재함")
    public void testAddReview_Failed_AlreadyExists() {
        // Given
        User user = new User("test1234@naver.com","testUser","test123!","test",false, UserRoleEnum.USER);
        Store store = new Store();
        Order order = new Order();
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(storeId, orderId, 5, "이미 존재하는 리뷰");

        when(userRepository.findUserByUserId("testUser")).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderIdAndIsDeletedFalse(orderId)).thenReturn(Optional.of(order));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        // 이미 해당 주문(Order)에 대한 리뷰가 존재한다고 가정
        when(reviewRepository.existsByOrderId(order)).thenReturn(true);

        // When & Then
        DuplicateReviewException exception = assertThrows(DuplicateReviewException.class, () ->
                reviewService.addReview(requestDto, "testUser")
        );

        assertEquals("해당 주문에 대한 리뷰가 존재합니다.", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }
    @Test
    @DisplayName("리뷰 등록 실패 - 리뷰 내용이 255자 초과")
    public void testAddReview_Failed_Over255Char() {
        // Given
        User user = new User("test1234@naver.com","testUser","test123!","test",false, UserRoleEnum.USER);
        Store store = new Store();
        Order order = new Order();

        // 256자 이상의 긴 문자열을 만든다.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            sb.append("a");
        }
        String longContent = sb.toString();

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(storeId, orderId, 5, longContent);

        when(userRepository.findUserByUserId("testUser")).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderIdAndIsDeletedFalse(orderId)).thenReturn(Optional.of(order));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.existsByOrderId(order)).thenReturn(false);

        // When & Then
        InvalidReviewContentException exception = assertThrows(InvalidReviewContentException.class, () ->
                reviewService.addReview(requestDto, "testUser")
        );


        assertEquals("리뷰내용은 최대 255자까지 가능합니다.", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

}
