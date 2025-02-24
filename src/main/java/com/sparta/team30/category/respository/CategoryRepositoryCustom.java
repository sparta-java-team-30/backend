package com.sparta.team30.category.respository;

import com.sparta.team30.category.domain.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    boolean isCategoryNameExists(String categoryName);
    List<Category> getCategories();
}
