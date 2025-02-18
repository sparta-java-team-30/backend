package com.sparta.team30.category.dto;

import com.sparta.team30.category.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    private UUID categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private Boolean isDeleted;

    public CategoryResponseDto(Category category){
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createdAt = category.getCreatedAt();
        this.createdBy = category.getCreatedBy();
        this.updatedAt = category.getUpdatedAt();
        this.updatedBy = category.getUpdatedBy();
        this.deletedAt = category.getDeletedAt();
        this.deletedBy = category.getDeletedBy();
        this.isDeleted = category.getIsDeleted();
    }
}