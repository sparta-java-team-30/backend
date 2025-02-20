package com.sparta.team30.store.dto;

import com.sparta.team30.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "음식점 생성 응답 DTO")
@Getter
@NoArgsConstructor
public class StoreCreateResponseDto {
    private UUID storeId;
    private UUID categoryId;
    private String storeName;
    private String storePhone;
    private String storePostcode;
    private String storeAddress1;
    private String storeAddress2;
    private Boolean isApproved;
    private LocalDateTime createdAt;

    public StoreCreateResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.categoryId = store.getCategory().getCategoryId();
        this.storeName = store.getStoreName();
        this.storePhone = store.getStorePhone();
        this.storePostcode = store.getStorePostcode();
        this.storeAddress1 = store.getStoreAddress1();
        this.storeAddress2 = store.getStoreAddress2();
        this.isApproved = store.getIsApproved();
        this.createdAt = store.getCreatedAt();
    }

}
