package com.sparta.team30.review.dto;


import com.sparta.team30.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private UUID reviewId;
    private UUID storeId;
    private UUID orderId;
    private Integer score;
    private String content;


    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.storeId = review.getStoreId().getStoreId();
        this.orderId = review.getOrderId().getOrderId();
        this.score = review.getScore();
        this.content = review.getContent();
    }
}