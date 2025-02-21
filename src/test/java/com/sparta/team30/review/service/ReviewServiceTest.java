package com.sparta.team30.review.service;

import com.sparta.team30.address.domain.Address;
import com.sparta.team30.category.domain.Category;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewCreateRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private User user;
    private Store store;
    private Order order;
    private Review review;
    private UUID reviewId;
    private UUID storeId;
    private UUID orderId;
    private Address address;
    private Category category;
    @BeforeEach
    void setUp() {

        reviewId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        ReviewCreateRequestDto userDto = new User("testUser", UserRoleEnum.Authority.CUSTOMER);


        StoreRequestDto requestDto = new StoreRequestDto(category.getCategoryId(), "한식당1", "0212341234", "01234", "서울시 중구", "1층");
        Store store = new Store(category, requestDto);
        storeId = UUID.randomUUID();

        // Order 초기화 (Setter 없음, 생성자 사용)
        orderId = UUID.randomUUID();

        // Review 초기화 (Setter 없음, 생성자 사용)
        review = new Review(5, "Great food!", store, order, user);
        reviewId = UUID.randomUUID();
    }

    @Test
    @DisplayName("리뷰 등록 성공 테스트")
    public void testAddReview() {
        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(storeId, orderId, 4, "Nice place!");
        when(userRepository.findUserByUserId("testUser")).thenReturn(Optional.of(user));
        when(orderRepository.findByOrderIdAndIsDeletedFalse(orderId)).thenReturn(Optional.of(order));
        when(storeRepository.findByStoreIdAndIsDeletedFalse(storeId)).thenReturn(Optional.of(store));
        when(reviewRepository.existsByOrderId(order)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // When
        ReviewResponseDto response = reviewService.addReview(requestDto, "testUser");

        // Then
        assertEquals(reviewId, response.getReviewId());
        assertEquals(4, response.getScore());
        assertEquals("Great food!", response.getContent());
        assertEquals(storeId, response.getStoreId());
        assertEquals(orderId, response.getOrderId());
        assertEquals(4.0, store.getStoreGrade()); // 첫 리뷰 반영
        assertEquals(1, store.getStoreReviewCount());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(storeRepository, times(1)).save(store);


    }




}