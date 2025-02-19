package com.sparta.team30.review.service;

import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.MockStoreRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.repository.UserRepository;
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
    private final MockStoreRepository mockStoreRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Review> findAllReviewByStore(UUID storeId/*, int page, int size, String sortBy, boolean isAsc*/) {
        //음식점 검사
        Store store = mockStoreRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 음식점이 존재하지 않습니다"));
//
//        //페이징네이션
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort= Sort.by(direction, sortBy);
//        //기본값 10 설정 조작 방지
//        int allowedSize = (size == 10 || size == 30 || size == 50) ? size : 10;
//        Pageable pageable= PageRequest.of(page,allowedSize,sort);

        //조회시 본인것만 조회 or 모두 조회 권한 enumRole User / ALL

        return reviewRepository.findAllByStoreAndIsDeletedFalse(storeId);

    }

    //등록
    @Transactional
    public Review addReview(ReviewRequestDto reviewRequestDto , String username) {
        User user = findUserByUserId(username);
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
    public void updateReview(ReviewRequestDto reviewRequestDto,String username) {
        //리뷰 존재 여부
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(reviewRequestDto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않거나 이미 삭제되었습니다."));
        //유저확인
        if (!review.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 사용자의 리뷰는 삭제할 수 없습니다.");
        }

        UUID storeId = reviewRequestDto.getStoreId();
        int score = review.getScore();
        updateStoreRating(storeId, score, reviewRequestDto.getScore());
    }


    //삭제 reviewId 받아서 해당 글 isDeleted true
    @Transactional
    public void deleteReview(UUID reviewId,String username) {
        //리뷰 존재 여부
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않거나 이미 삭제되었습니다."));

        //유저확인
        if (!review.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 사용자의 리뷰는 삭제할 수 없습니다.");
        }


        // 삭제된 리뷰의 storeId와 평점 가져오기
        UUID storeId = review.getStoreId().getStoreId();
        int deletedScore = review.getScore();

        // 소프트 삭제
        review.deleteReview(username);

        // 가게 평점 재계산 (삭제된 평점을 반영)
        updateStoreRating(storeId, deletedScore, 0);
    }



    //엔티티 검증 공통메서드
    private User findUserByUserId(String username) {
        return userRepository.findUserByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Order findOrderById(UUID order) {
        return orderRepository.findByOrderIdAndIsDeletedFalse(order)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
    }

    private Store findStoreById(UUID storeId) {
        return mockStoreRepository.findByStoreIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장이 존재하지 않습니다."));
    }

    private void validateReviewUniqueness(Order order) {
        if (reviewRepository.existsByOrderId(order)) {
            throw new IllegalArgumentException("해당 주문에 대한 리뷰가 존재합니다.");
        }
    }
    //리뷰 재계산
    private void updateStoreRating(UUID storeId, int oldScore, int newScore) {
        Store store = findStoreById(storeId);

        // 현재 리뷰 수와 평균 평점 가져오기
        int currentReviewCount = store.getStoreReviewCount();
        double currentAverage = store.getStoreGrade();

        if (currentReviewCount <= 0) {
            throw new IllegalStateException("리뷰가 존재하지 않습니다.");
        }

        // 새로운 평균 계산
        // (기존 평균 * 기존 리뷰 수 - 기존 평점 + 새 평점) / (리뷰 수)
        double newAverage;
        if (newScore == 0) { // 삭제 시
            newAverage = (currentAverage * currentReviewCount - oldScore) / (currentReviewCount - 1);
            store.setStoreReviewCount(currentReviewCount - 1);
        } else { // 수정 시
            newAverage = (currentAverage * currentReviewCount - oldScore + newScore) / currentReviewCount;
        }

        // 소수점 한 자리로 반올림
        double roundedAverage = Math.round(newAverage * 10) / 10.0;

        // 음수 또는 NaN 체크
        if (Double.isNaN(roundedAverage) || roundedAverage < 0) {
            roundedAverage = 0.0;
        }

        // 가게 엔티티 업데이트
        store.setStoreGrade(roundedAverage);
        if (newScore == 0) { // 삭제 시 리뷰 수 감소
            store.setStoreReviewCount(currentReviewCount - 1);
        }

        mockStoreRepository.save(store);
    }




}