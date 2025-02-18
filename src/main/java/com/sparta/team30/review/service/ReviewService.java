package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.StoreRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public List<Review> findAllReviewByStore(UUID storeId/*, int page, int size, String sortBy, boolean isAsc*/) {
        //음식점 검사
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 음식점이 존재하지 않습니다"));
//
//        //페이징네이션
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort= Sort.by(direction, sortBy);
//        //기본값 10 설정 조작 방지
//        int allowedSize = (size == 10 || size == 30 || size == 50) ? size : 10;
//        Pageable pageable= PageRequest.of(page,allowedSize,sort);

        //조회시 본인것만 조회 or 모두 조회 권한 enumRole User / ALL

        return reviewRepository.findAllByStoreAndIsDeletedFalse(store);

    }

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

        // 음식점 리뷰 최초 등록 시 누적평균 평점 초기화
        // 이후 리뷰가 추가될 경우 누적평균을 업데이트 함.
        store.addReviewScore(reviewRequestDto.getScore());

        return reviewRepository.save(review);
    }

    //수정 reviewId socre content 받아서 해당 글 수정 put
    @Transactional
    public void updateReview(ReviewRequestDto reviewRequestDto , User user) {

    }


    //삭제 reviewId 받아서 해당 글 isDeleted true
    @Transactional
    public void deleteReview(Store reviewId) {
        Review review = reviewRepository.findByStoreIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않거나 이미 삭제되었습니다."));

        //관리자/유저만 지울수있게 유저는 3일이내

        //리뷰 소프트 삭제 처리
        reviewRepository.deleteByReviewId(review.getReviewId());

        //평점 재계산
        UpdateStoreRatingUpdate(review.getStoreId().getStoreId());
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
        if (reviewRepository.existsByOrderId(order)) {
            throw new IllegalArgumentException("해당 주문에 대한 리뷰가 존재합니다.");
        }
    }

    private void UpdateStoreRatingUpdate(UUID storeId) {
        Store store = findStoreById(storeId);

        Double newAvgRating = reviewRepository.calculatingAvgRating(storeId);
        int newReviewCount = reviewRepository.countByStoreIdAndIsDeletedFalse(store);

        store.UpdateAvgRating(newAvgRating != null ? newAvgRating : 0.0);
        store.UpdateReviewCount(newReviewCount);
        storeRepository.save(store);

    }




}