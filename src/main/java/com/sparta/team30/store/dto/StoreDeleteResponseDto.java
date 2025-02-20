package com.sparta.team30.store.dto;

import com.sparta.team30.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "음식점 삭제 응답 DTO")
@Getter
@NoArgsConstructor
public class StoreDeleteResponseDto {
    private UUID storeId;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private Boolean isDeleted;

    public StoreDeleteResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.deletedAt = store.getDeletedAt();
        this.deletedBy = store.getDeletedBy();
        this.isDeleted = store.getIsDeleted();
    }
}
