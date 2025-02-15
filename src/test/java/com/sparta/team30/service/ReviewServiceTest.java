package com.sparta.team30.service;

import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.OrderRepository;
import com.sparta.team30.review.repository.StoreRepository;
import com.sparta.team30.review.repository.UserRepository;
import com.sparta.team30.review.service.ReviewService;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito를 사용하기 위한 설정
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService; // Mock된 객체들을 주입할 서비스

    private UUID userId;
    private UUID orderId;
    private UUID storeId;

    private User mockUser;
    private Order mockOrder;
    private Store mockStore;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        storeId = UUID.randomUUID();

        mockUser = mock(User.class);
        mockOrder = mock(Order.class);
        mockStore = mock(Store.class);
    }

    @Test
    @DisplayName("리뷰 등록 성공 테스트")
    void testAddReviewSuccess() {
        // Given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(storeId, orderId, 5, "맛있어요!");


        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
        when(mockStore.getStoreId()).thenReturn(storeId);
        when(mockOrder.getOrderId()).thenReturn(orderId);

        when(reviewRepository.existsByOrderId(mockOrder)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review review = invocation.getArgument(0);
            return new Review(
                    UUID.randomUUID(),
                    review.getScore(),
                    review.getContent(),
                    review.getUser(),
                    review.getStoreId(),
                    review.getOrderId()
            );
        });


        // When
        ReviewResponseDto response = reviewService.addReview(reviewRequestDto, userId);

        // Then
        assertNotNull(response);
        assertEquals(storeId, response.getStoreId());
        assertEquals(orderId, response.getOrderId());
        assertEquals(5, response.getScore());
        assertEquals("맛있어요!", response.getContent());

        // Mock 객체의 메서드가 정확히 호출되었는지 검증
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findById(orderId);
        verify(storeRepository, times(1)).findById(storeId);
        verify(reviewRepository, times(1)).existsByOrderId(mockOrder);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 등록 실패 - 존재하지 않는 유저")
    void testAddReview_UserNotFound() {
        // Given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(storeId, orderId, 5, "맛있어요!");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(reviewRequestDto, userId);
        });

        assertEquals("해당 유저가 존재하지 않습니다.", exception.getMessage());

        // verify()를 사용해 실제로 `findById`가 호출되었는지 확인
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, never()).findById(any());
        verify(storeRepository, never()).findById(any());
        verify(reviewRepository, never()).existsByOrderId(any());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("리뷰 등록 실패 - 존재하지 않는 주문")
    void testAddReview_OrderNotFound() {
        // Given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(storeId, orderId, 5, "맛있어요!");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(reviewRequestDto, userId);
        });

        assertEquals("해당 주문이 존재하지 않습니다.", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findById(orderId);
        verify(storeRepository, never()).findById(any());
        verify(reviewRepository, never()).existsByOrderId(any());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("리뷰 등록 실패 - 주문당 리뷰 1개 제한")
    void testAddReview_ReviewAlreadyExists() {
        // Given
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(storeId, orderId, 5, "맛있어요!");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
        when(reviewRepository.existsByOrderId(mockOrder)).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(reviewRequestDto, userId);
        });

        assertEquals("해당 주문에 대한 리뷰가 존재합니다.", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findById(orderId);
        verify(storeRepository, times(1)).findById(storeId);
        verify(reviewRepository, times(1)).existsByOrderId(mockOrder);
        verify(reviewRepository, never()).save(any());
    }
}
