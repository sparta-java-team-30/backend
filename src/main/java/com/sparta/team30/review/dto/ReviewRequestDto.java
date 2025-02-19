package com.sparta.team30.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private UUID reviewId;
    private UUID storeId;
    private UUID orderId;
    private Integer score;
    private String content;


}
