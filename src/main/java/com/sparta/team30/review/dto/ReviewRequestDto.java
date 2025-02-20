package com.sparta.team30.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private UUID reviewId;

    @Schema(description = "가게 고유 ID", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID storeId;

    @Schema(description = "주문 고유 ID", example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID orderId;

    @Schema(description = "리뷰 점수 (1~5)", example = "5", minimum = "1", maximum = "5")
    private Integer score;

    @Schema(description = "리뷰 내용", example = "정말 맛있어요!", maxLength = 255)
    private String content;


}
