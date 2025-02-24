package com.sparta.team30.category.dto;

import com.sparta.team30.category.domain.Category;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoryDto {
    private UUID categoryId;
    private String categoryName;

    public CategoryDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }
}
