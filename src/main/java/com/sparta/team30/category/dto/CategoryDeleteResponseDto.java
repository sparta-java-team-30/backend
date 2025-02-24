package com.sparta.team30.category.dto;

import com.sparta.team30.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "카테고리 삭제 응답 DTO")
@Getter
@NoArgsConstructor
public class CategoryDeleteResponseDto {
    private UUID categoryId;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private Boolean isDeleted;

    public CategoryDeleteResponseDto(Category category){
        this.categoryId = category.getCategoryId();
        this.deletedAt = category.getDeletedAt();
        this.deletedBy = category.getDeletedBy();
        this.isDeleted = category.getIsDeleted();
    }
}
