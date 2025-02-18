package com.sparta.team30.review.controller;

import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.service.ReviewService;
import com.sparta.team30.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    //리뷰등록
    @PostMapping("/create")
    public ResponseEntity<Review> CreateReview(@RequestBody ReviewRequestDto requestDto,@RequestParam User user/*,@AuthenticationPrincipal UserDetailsImpl userDetails*/) {
        Review response = reviewService.addReview(requestDto,user);
        return ResponseEntity.ok(response);
    }


    }
