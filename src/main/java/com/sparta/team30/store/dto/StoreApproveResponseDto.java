package com.sparta.team30.store.dto;

import com.sparta.team30.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "음식점 승인 응답 DTO")
@Getter
@NoArgsConstructor
public class StoreApproveResponseDto {
    private UUID storeId;
    private UUID categoryId;
    private String storeName;
    private Boolean isApproved;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public StoreApproveResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.categoryId = store.getCategory().getCategoryId();
        this.storeName = store.getStoreName();
        this.isApproved = store.getIsApproved();
        this.updatedAt = store.getUpdatedAt();
        this.updatedBy = store.getUpdatedBy();
    }
}
