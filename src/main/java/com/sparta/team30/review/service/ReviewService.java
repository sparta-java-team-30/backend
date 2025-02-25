package com.sparta.team30.review.service;

import com.sparta.team30.common.exception.OrderNotFoundException;
import com.sparta.team30.common.exception.StoreNotFoundException;
import com.sparta.team30.order.domain.Order;
import com.sparta.team30.order.repository.OrderRepository;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewCreateRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.dto.ReviewUpdateRequestDto;
import com.sparta.team30.review.exception.ReviewAccessDeniedException;
import com.sparta.team30.review.exception.ReviewNotFoundException;
import com.sparta.team30.review.exception.ReviewTimeExpiredException;
import com.sparta.team30.review.repository.ReviewRepository;
import com.sparta.team30.store.domain.Store;
import com.sparta.team30.store.repository.StoreRepository;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Review> findAllReviewByStore(UUID storeId,String keyword,Pageable pageable) {
        // 음식점 검사
        findStoreById(storeId);

        return reviewRepository.findAllByStoreIdAndIsDeletedFalseWithKeyword(storeId,keyword, pageable);
    }

    //사용자 개인 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> findAllReviewByUser(String username) {
        User user = findUserByUserId(username);
        List<Review> reviews = reviewRepository.findAllByUserIdAndIsDeletedFalse(user.getId());
        return reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    //등록
    @Transactional
    public ReviewResponseDto addReview(ReviewCreateRequestDto requestDto, String username) {
        User user = findUserByUserId(username);
        User targetUser = findUserByUserId(username);

        // 본인 확인 (직접 작성하고 있는 계정과, 조회하려는 계정이 다른 경우)
        if (!user.getUsername().equals(targetUser.getUsername())) {
            throw new ReviewAccessDeniedException("본인만 조회할 수 있습니다.");
        }

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

    //수정
    @Transactional
    public void updateReview(ReviewUpdateRequestDto requestDto, String username) {
        //리뷰 존재 여부
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(requestDto.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없거나 이미 삭제된 리뷰입니다."));
        if (!review.getUser().getUsername().equals(username)) {
            throw new ReviewAccessDeniedException("본인만 수정할 수 있습니다.");
        }

        if (requestDto.getScore() < 0 || requestDto.getScore() > 5) {
            throw new IllegalArgumentException("유효한 점수 범위를 입력해주세요.");
        }
        if (requestDto.getContent() == null || requestDto.getContent().length() > 255) {
            throw new IllegalArgumentException("리뷰내용은 255자를 넘을 수 없습니다.");
        }
        //유저확인
        // 사용자 역할 및 권한 확인
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = review.getCreatedAt();
        if (createdAt.isBefore(now.minusDays(3))) {
            throw new ReviewTimeExpiredException("리뷰는 생성 후 3일 이내에만 수정할 수 있습니다.");
        }

        int oldScore = review.getScore();
        review.updateReview(requestDto.getScore(), requestDto.getContent());

        updateStoreRating(review.getStoreId().getStoreId(), oldScore, requestDto.getScore());
    }


    //삭제
    @Transactional
    public void deleteReview(UUID reviewId,String username) {
        //리뷰 존재 여부
        Review review = reviewRepository.findByReviewIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰가 존재하지 않거나 이미 삭제되었습니다."));

        // 사용자 정보 가져오기
        User user = review.getUser();
        String userAuthority = user.getRole().getAuthority();

        // 권한 검증
        if (userAuthority.equals(UserRoleEnum.Authority.CUSTOMER)) {
            if (!user.getUsername().equals(username)) {
                throw new ReviewAccessDeniedException("본인의 리뷰만 삭제할 수 있습니다.");
            }
        } else if (userAuthority.equals(UserRoleEnum.Authority.MASTER)) {
            // MASTER는 모든 리뷰 삭제 가능
        } else {
            throw new ReviewAccessDeniedException("리뷰 삭제는 CUSTOMER 또는 MASTER 권한만 가능합니다.");
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
                .orElseThrow(() -> new OrderNotFoundException("해당 주문이 존재하지 않습니다."));
    }

    private Store findStoreById(UUID storeId) {
        return storeRepository.findByStoreIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new StoreNotFoundException("해당 매장이 존재하지 않습니다."));
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
        double newAverage;
        if (newScore == 0) { // 삭제 시
            if (currentReviewCount <= 1) {
                newAverage = 0.0; // 마지막 리뷰 삭제 시 0점 처리
            } else {
                newAverage = (currentAverage * currentReviewCount - oldScore) / (currentReviewCount - 1);
            }
            store.setStoreReviewCount(currentReviewCount - 1);
        } else { // 수정 시
            newAverage = (currentAverage * currentReviewCount - oldScore + newScore) / currentReviewCount;
        }

        // 소수점 한 자리로 반올림
        double roundedAverage = Math.round(newAverage * 10) / 10.0;

        // 5점 초과 방지 (newAverage가 5를 초과할 때만 적용)
        if (roundedAverage > 5.0) {
            roundedAverage = 5.0;
        }

        // 음수 또는 NaN 체크
        if (Double.isNaN(roundedAverage) || roundedAverage < 0) {
            roundedAverage = 0.0;
        }

        // 가게 엔티티 업데이트
        store.setStoreGrade(roundedAverage);
        if (newScore == 0) { // 삭제 시 리뷰 수 감소
            store.setStoreReviewCount(currentReviewCount - 1);
        }

        storeRepository.save(store);
    }




}