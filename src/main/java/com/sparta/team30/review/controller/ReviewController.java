package com.sparta.team30.review.controller;

import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.review.domain.Review;
import com.sparta.team30.review.dto.ReviewCreateRequestDto;
import com.sparta.team30.review.dto.ReviewResponseDto;
import com.sparta.team30.review.dto.ReviewUpdateRequestDto;
import com.sparta.team30.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "Review API", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    //리뷰등록

    @PostMapping("/create")
    @Operation(summary = "리뷰 생성", description = "새로운 리뷰를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 유효하지 않은 데이터)"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 해당 주문에 리뷰가 존재함")
    })
    public ResponseEntity<ReviewResponseDto> CreateReview(
            @Parameter(description = "리뷰 생성 요청 DTO", required = true) @RequestBody ReviewCreateRequestDto requestDto,
            @Parameter(hidden = true, description = "현재 인증된 사용자") @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        ReviewResponseDto response = reviewService.addReview(requestDto, username);
        return ResponseEntity.ok(response);
    }

    //음식점 내 리뷰 조회
    @GetMapping("/{storeId}/reviews")
    @Operation(summary = "가게별 리뷰 목록 조회", description = "특정 가게의 리뷰를 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록이 성공적으로 조회됨"),
            @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음")
    })
    public ResponseEntity<Page<ReviewResponseDto>> getAllReviewsByStore(
            @PathVariable UUID storeId,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준 필드", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순/내림차순", example = "true") @RequestParam(defaultValue = "true") boolean isAsc,
            @Parameter(description = "리뷰 내용 키워드", example = "맛있") @RequestParam(required = false) String keyword) {

        // 페이징 및 정렬 조건 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 서비스 호출
        Page<Review> reviews = reviewService.findAllReviewByStore(storeId, keyword, pageable);

        // Review 엔티티를 ReviewResponseDto로 변환
        Page<ReviewResponseDto> response = reviews.map(ReviewResponseDto::new);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-reviews")
    @Operation(summary = "내 리뷰 목록 조회", description = "현재 로그인한 사용자의 리뷰 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록이 성공적으로 조회됨"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "리뷰 데이터를 찾을 수 없음")
    })
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        List<ReviewResponseDto> reviews = reviewService.findAllReviewByUser(username);
        return ResponseEntity.ok(reviews);
    }


    //    //수정
    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "특정 리뷰를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
    })
    public ResponseEntity<String> updateReview(@Parameter(description = "리뷰 수정 요청 DTO") @RequestBody ReviewUpdateRequestDto requestDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        reviewService.updateReview(requestDto, username);
        return ResponseEntity.ok("리뷰가 수정되었습니다.");
    }


    //소프트삭제

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 소프트 삭제", description = "특정 리뷰를 소프트 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "리뷰가 성공적으로 삭제됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
    })
    public ResponseEntity<String> deleteReview(@Parameter(description = "삭제할 리뷰의 ID") @PathVariable UUID reviewId,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        reviewService.deleteReview(reviewId, username);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

}




