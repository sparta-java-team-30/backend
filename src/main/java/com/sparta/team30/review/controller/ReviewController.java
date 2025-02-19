package com.sparta.team30.review.controller;

import com.sparta.team30.infrastructure.security.UserDetailsImpl;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    //리뷰등록
    @PostMapping("/create")
    public ResponseEntity<Review> CreateReview(@RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        Review response = reviewService.addReview(requestDto,username);
        return ResponseEntity.ok(response);
    }

    //음식점 내 리뷰 조회
    @GetMapping("/{storeId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getAllReviewsByStore(@PathVariable UUID storeId) {
        // 서비스에서 해당 음식점의 리뷰 목록 조회 (페이징/검색은 나중에 추가)
        List<Review> reviews = reviewService.findAllReviewByStore(storeId);

        // Review 엔티티를 ReviewResponseDto로 변환
        List<ReviewResponseDto> response = reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

//    //수정
//    @PutMapping("/{}")
//    public ResponseEntity<String> updateReview(@RequestBody ReviewRequestDto requestDto/*, @AuthenticationPrincipal UserDetails userDetails*/) {
//         reviewService.updateReview(requestDto, user);
//        return ResponseEntity.ok("리뷰가 수정되었습니다.");
//    }
//
//
    //소프트삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable UUID reviewId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        reviewService.deleteReview(reviewId,username);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

}




