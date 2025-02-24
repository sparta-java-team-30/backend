package com.sparta.team30.category.service;

import com.sparta.team30.category.domain.Category;
import com.sparta.team30.category.dto.*;
import com.sparta.team30.category.exception.CategoryAlreadyDeleteException;
import com.sparta.team30.category.exception.CategoryNotFoundException;
import com.sparta.team30.category.exception.DuplicateCategoryException;
import com.sparta.team30.category.respository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryListResponseDto> getCategories() {
        List<Category> categoryList = categoryRepository.getCategories();
        List<CategoryListResponseDto> categoryResponseDtoList = new ArrayList<>();

        for (Category c : categoryList) {
            categoryResponseDtoList.add(new CategoryListResponseDto(c));
        }
        return categoryResponseDtoList;
    }

    @Transactional
    public CategoryCreateResponseDto createCategory(CategoryRequestDto requestDto) {
        isDuplicated(requestDto.getCategoryName());

        Category category = categoryRepository.save(new Category(requestDto));
        return new CategoryCreateResponseDto(category);
    }


    @Transactional
    public CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto requestDto) {
        isDuplicated(requestDto.getCategoryName());

        Category category = categoryFindById(categoryId);
        category.update(requestDto);
        return new CategoryResponseDto(category);
    }

    @Transactional
    public CategoryDeleteResponseDto deleteCategory(UUID categoryId, String deletedBy) {
        Category category = categoryFindById(categoryId);

        if(category.getIsDeleted()) {
            throw new CategoryAlreadyDeleteException("이미 삭제한 카테고리입니다.");
        }

        category.delete(deletedBy);
        return new CategoryDeleteResponseDto(category);
    }

    public Category categoryFindById(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException("카테고리가 존재하지 않습니다.")
        );
    }

    public boolean isDuplicated(String categoryName) {
        boolean result = categoryRepository.isCategoryNameExists(categoryName);
        if (result) {
            throw new DuplicateCategoryException("중복된 카테고리가 있습니다.");
        }

        return result;
    }

}

