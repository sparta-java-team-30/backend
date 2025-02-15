package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.review.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.StoreRepository;
import com.sparta.team30.review.repository.UserRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
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
    private final UserRepository userRepository;

    //등록
    @Transactional
    public ReviewResponseDto addReview(ReviewRequestDto reviewRequestDto , UUID id) {

        // 유저검증
        //jwt 헤더 토큰
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        //실제 주문이 있나?
        //orderRepository 에서 FindById 로 getOrederId
        Order order = orderRepository.findById(reviewRequestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        //음식점이 존재 하나?
        //storeRepository에서 FindById getStoreId
        Store store = storeRepository.findById(reviewRequestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));

        //주문당 리뷰 1개 제한
        //ExistsByOrder
        if(reviewRepository.existsByOrderId(order)){
            throw new IllegalArgumentException("해당 주문에 대한 리뷰가 존재합니다.");
        }

        if (reviewRequestDto.getContent().length() > 255){
            throw new IllegalArgumentException("리뷰내용은 최대 255자까지 가능합니다.");
        }

        //리뷰 저장
        Review review = new Review(reviewRequestDto.getScore(), reviewRequestDto.getContent(),user,store, order);

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDto(
                savedReview.getReviewId(),
                savedReview.getStoreId() != null ? savedReview.getStoreId().getStoreId() : null, // 널 체크 추가
                savedReview.getOrderId() != null ? savedReview.getOrderId().getOrderId() : null, // 널 체크 추가
                savedReview.getScore(),
                savedReview.getContent()
        );
    }


}
