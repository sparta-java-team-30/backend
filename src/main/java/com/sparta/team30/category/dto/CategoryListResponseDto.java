package com.sparta.team30.category.dto;

import com.sparta.team30.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "카테고리 조회 응답 DTO")
@Getter
@NoArgsConstructor
public class CategoryListResponseDto {
    private UUID categoryId;
    private String categoryName;

    public CategoryListResponseDto(Category category){
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }
}
