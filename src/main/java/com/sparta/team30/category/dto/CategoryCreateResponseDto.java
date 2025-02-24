package com.sparta.team30.category.dto;

import com.sparta.team30.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "카테고리 생성 응답 DTO")
@Getter
@NoArgsConstructor
public class CategoryCreateResponseDto {
    private UUID categoryId;
    private String categoryName;
    private LocalDateTime createdAt;

    public CategoryCreateResponseDto(Category category){
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createdAt = category.getCreatedAt();
    }
}
