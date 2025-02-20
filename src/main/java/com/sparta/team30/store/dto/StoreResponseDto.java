package com.sparta.team30.store.dto;

import com.sparta.team30.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "음식점 응답 DTO")
@Getter
@NoArgsConstructor
public class StoreResponseDto {
    private UUID storeId;
    private UUID categoryId;
    private String storeName;
    private String storePhone;
    private String storePostcode;
    private String storeAddress1;
    private String storeAddress2;
    private double storeGrade;
    private int storeReviewCount;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private Boolean isDeleted;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.categoryId = store.getCategory().getCategoryId();
        this.storeName = store.getStoreName();
        this.storePhone = store.getStorePhone();
        this.storePostcode = store.getStorePostcode();
        this.storeAddress1 = store.getStoreAddress1();
        this.storeAddress2 = store.getStoreAddress2();
        this.storeGrade = store.getStoreGrade();
        this.storeReviewCount = store.getStoreReviewCount();
        this.isApproved = store.getIsApproved();
        this.createdAt = store.getCreatedAt();
        this.createdBy = store.getCreatedBy();
        this.updatedAt = store.getUpdatedAt();
        this.updatedBy = store.getUpdatedBy();
        this.deletedAt = store.getDeletedAt();
        this.deletedBy = store.getDeletedBy();
        this.isDeleted = store.getIsDeleted();
    }
}
