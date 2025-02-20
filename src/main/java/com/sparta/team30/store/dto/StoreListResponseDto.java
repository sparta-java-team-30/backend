package com.sparta.team30.store.dto;

import com.sparta.team30.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "음식점 조회 응답 DTO")
@Getter
@NoArgsConstructor
public class StoreListResponseDto {
    private UUID storeId;
    private UUID categoryId;
    private String storeName;
    private String storePhone;
    private double storeGrade;
    private int storeReviewCount;
    private Boolean isApproved;

    public StoreListResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.categoryId = store.getCategory().getCategoryId();
        this.storeName = store.getStoreName();
        this.storePhone = store.getStorePhone();
        this.storeGrade = store.getStoreGrade();
        this.storeReviewCount = store.getStoreReviewCount();
        this.isApproved = store.getIsApproved();
    }
}
