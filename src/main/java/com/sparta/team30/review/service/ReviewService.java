package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.StoreRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    //등록
    @Transactional
    public Review addReview(ReviewRequestDto reviewRequestDto , User user) {



        Order order = findOrderById(reviewRequestDto.getOrderId());
        Store store = findStoreById(reviewRequestDto.getStoreId());
        validateReviewUniqueness(order);

        if (reviewRequestDto.getContent().length() > 255){
            throw new IllegalArgumentException("리뷰내용은 최대 255자까지 가능합니다.");
        }

        Review review = new Review(
                reviewRequestDto.getScore(),
                reviewRequestDto.getContent(),
                store,
                order,
                user
        );
        return reviewRepository.save(review);
    }


    //엔티티 검증 공통메서드
    private Order findOrderById(UUID order) {
        return orderRepository.findByOrderIdAndIsDeletedFalse(order)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }

    private Store findStoreById(UUID store) {
        return storeRepository.findByStoreIdAndIsDeletedFalse(store)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
    }

    private void validateReviewUniqueness(Order order) {
        if (reviewRepository.existsByOrderId(order.getOrderId())) {
            throw new IllegalArgumentException("해당 주문에 대한 리뷰가 존재합니다.");
        }
    }




}
