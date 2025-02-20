package com.sparta.team30.review.service;

import com.sparta.team30.common.exception.ReviewAccessDeniedException;
import com.sparta.team30.common.exception.ReviewTimeExpiredException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewCreateRequestDto;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.dto.ReviewUpdateRequestDto;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.review.repository.MockStoreRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final MockStoreRepository mockStoreRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Review> findAllReviewByStore(UUID storeId,String keyword,Pageable pageable) {
        // 음식점 검사
        Store store = mockStoreRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 음식점이 존재하지 않습니다"));

        return reviewRepository.findAllByStoreIdAndIsDeletedFalseWithKeyword(storeId,keyword, pageable);
    }

    //등록
    @Transactional
    public ReviewResponseDto addReview(ReviewCreateRequestDto requestDto, String username) {
        User user = findUserByUserId(username);
        Order order = findOrderById(requestDto.getOrderId());
        Store store = findStoreById(requestDto.getStoreId());
        validateReviewUniqueness(order);

        if (requestDto.getContent().length() > 255) {
            throw new IllegalArgumentException("리뷰내용은 최대 255자까지 가능합니다.");
        }

        Review review = new Review(
                requestDto.getScore(),
                requestDto.getContent(),
                store,
                order,
                user
        );

        // 음식점 리뷰 최초 등록 시 누적평균 평점 초기화
        // 이후 리뷰가 추가될 경우 누적평균을 업데이트 함.
        store.addReviewScore(requestDto.getScore());

        Review savedReview = reviewRepository.save(review);

        return new ReviewResponseDto(savedReview);
    }

    //수정 reviewId socre content 받아서 해당 글 수정 put
    @Transactional
    public void updateReview(ReviewUpdateRequestDto RequestDto, String username) {
        //리뷰 존재 여부
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(RequestDto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않거나 이미 삭제되었습니다."));
        if (RequestDto.getScore() < 0 || RequestDto.getScore() > 5) { // 점수 범위 가정
            throw new IllegalArgumentException("유효한 점수 범위를 입력해주세요.");
        }
        //유저확인
        // 사용자 역할 및 권한 확인
        User user = review.getUser();
        String userAuthority = user.getRole().getAuthority(); // UserRoleEnum에서 권한 가져오기

        if (userAuthority.equals(UserRoleEnum.Authority.CUSTOMER)) { // USER 역할
            // 사용자 본인 확인
            if (!user.getUsername().equals(username)) {
                throw new ReviewAccessDeniedException("권한이 없습니다.");
            }

            // 생성 후 3일 이내인지 확인
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = review.getCreatedAt();
            if (createdAt.isBefore(now.minusDays(3))) {
                throw new ReviewTimeExpiredException("리뷰는 생성 후 3일 이내에만 수정할 수 있습니다.");
            }

        } else if (userAuthority.equals(UserRoleEnum.Authority.MANAGER)) { // MANAGER 역할
            // MANAGER는 시간 제한 없이 수정 가능, 하지만 현재 사용자와 동일해야 함
            if (!username.equals("admin")) { // MANAGER의 특정 사용자 이름 조건
                throw new ReviewAccessDeniedException("관리자만 리뷰를 수정할 수 있습니다.");
            }
        } else {
            throw new ReviewAccessDeniedException("해당 작업은 USER 또는 MANAGER 역할만 수행할 수 있습니다.");
        }

        UUID storeId = review.getStoreId().getStoreId();
        int score = review.getScore();
        updateStoreRating(storeId, score, RequestDto.getScore());
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