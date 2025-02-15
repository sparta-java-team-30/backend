package com.sparta.team30.review.controller;

import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    //리뷰등록
    @PostMapping("/review/create")
    public ResponseEntity<ReviewResponseDto> CreateReview(@RequestBody ReviewRequestDto requestDto, @RequestParam UUID id/*,@AuthenticationPrincipal UserDetailsImpl userDetails*/) {
        ReviewResponseDto response = reviewService.addReview(requestDto, id);
        return ResponseEntity.ok(response);
    }
/*
    //음식점건 리뷰조회
    @GetMapping("/review/reviewList")
    public List<ReviewResponseDto> StoreReviewList() {

        return null;
    }
    /*
    @PutMapping("/review/updateReview")
    public String updateReview() {
        return "null";
    }
    */

    }
